package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.datatypes.Datatypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class BerkeleyPostingsIterator implements CloseableIterator<Datatypes.Posting> {
    private final Cursor cursor;
    private DatabaseEntry keyEntry = new DatabaseEntry();
    private DatabaseEntry valueEntry = new DatabaseEntry();
    private final long termID;
    private long currentDocumentID;
    private ByteArrayInputStream inputStream;

    BerkeleyPostingsIterator(Database indexDB, long termID) {
        this.termID = termID;
        this.cursor = indexDB.openCursor(null, new CursorConfig().setReadUncommitted(true));
        LongBinding.longToEntry(termID, keyEntry);
        if (this.cursor.getSearchKey(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.NOTFOUND || LongBinding.entryToLong(keyEntry) != termID) {
            keyEntry = null;
        } else {
            readNextPostingsList();
        }
    }

    BerkeleyPostingsIterator(Database indexDB, long termID, long documentID) {
        this.termID = termID;
        this.cursor = indexDB.openCursor(null, new CursorConfig().setReadUncommitted(true));
        LongBinding.longToEntry(termID, keyEntry);
        LongBinding.longToEntry(documentID, valueEntry);
        if (this.cursor.getSearchBothRange(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.NOTFOUND || LongBinding.entryToLong(keyEntry) != termID) {
            keyEntry = null;
        } else {
            readNextPostingsList();
        }
    }

    private void readNextPostingsList() {
        TupleInput tupleInput = new TupleInput(valueEntry.getData());
        currentDocumentID = tupleInput.readLong();
        byte[] postingsBytes = new byte[tupleInput.available()];
        tupleInput.readFast(postingsBytes);
        inputStream = new ByteArrayInputStream(postingsBytes);
    }

    @Override
    public void close() throws Exception {
        cursor.close();
    }

    @Override
    public boolean hasNext() {
        return  keyEntry != null;
    }

    @Override
    public Datatypes.Posting next() {
        Datatypes.Posting result;
        try {
            result = Datatypes.Posting.parseDelimitedFrom(inputStream);
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }

        advance();

        return result;
    }

    private void advance() {
        if (inputStream.available() == 0) {
            if (cursor.getNext(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.NOTFOUND || LongBinding.entryToLong(keyEntry) != termID) {
                keyEntry = null;
            } else {
                readNextPostingsList();
            }
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
