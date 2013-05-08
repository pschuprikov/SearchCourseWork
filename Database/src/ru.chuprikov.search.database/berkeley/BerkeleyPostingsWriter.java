package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.PostingsWriter;
import ru.chuprikov.search.database.datatypes.Datatypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class BerkeleyPostingsWriter implements PostingsWriter {
    private final Database indexDB;

    private final DatabaseEntry keyEntry = new DatabaseEntry();
    private final DatabaseEntry valueEntry = new DatabaseEntry();
    private final ByteArrayOutputStream outputStream;
    private final int maxPostingsChunkSizeBytes;

    BerkeleyPostingsWriter(Database indexDB, long termID, int maxPostingsChunkSizeBytes) throws DatabaseException, IOException {
        this.indexDB = indexDB;

        outputStream = new ByteArrayOutputStream(maxPostingsChunkSizeBytes);
        this.maxPostingsChunkSizeBytes = maxPostingsChunkSizeBytes;

        Cursor cursor = null;
        try {
            cursor = indexDB.openCursor(null, CursorConfig.DEFAULT);
            LongBinding.longToEntry(termID + 1, keyEntry);

            OperationStatus status;
            if (cursor.getSearchKeyRange(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS)
                status = cursor.getPrev(keyEntry, valueEntry, LockMode.DEFAULT);
            else
                status = cursor.getLast(keyEntry, valueEntry, LockMode.DEFAULT);

            if (status == OperationStatus.SUCCESS && LongBinding.entryToLong(keyEntry) == termID)
                outputStream.write(valueEntry.getData());

            LongBinding.longToEntry(termID, keyEntry);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public void appendPosting(Datatypes.Posting posting) throws IOException, DatabaseException {
        if (outputStream.size() + 8 + posting.getSerializedSize() > maxPostingsChunkSizeBytes) {
            flush();
        }

        if (outputStream.size() == 0) {
            LongBinding.longToEntry(posting.getDocumentID(), valueEntry);
            outputStream.write(valueEntry.getData());
        }

        posting.writeDelimitedTo(outputStream);
    }

    private void flush() throws DatabaseException {
        valueEntry.setData(outputStream.toByteArray());
        indexDB.put(null, keyEntry, valueEntry);
        outputStream.reset();
    }

    @Override
    public void close() throws Exception {
        if (outputStream.size() != 0) {
            flush();
        }
    }
}
