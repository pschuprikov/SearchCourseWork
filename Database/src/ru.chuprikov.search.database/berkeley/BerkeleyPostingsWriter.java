package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.PostingsWriter;
import ru.chuprikov.search.datatypes.Datatypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class BerkeleyPostingsWriter implements PostingsWriter {
    private final Database indexDB;

    private final DatabaseEntry keyEntry = new DatabaseEntry();
    private final DatabaseEntry valueEntry = new DatabaseEntry();
    private final ByteArrayOutputStream outputStream;
    private final int maxPostingsChunkSizeBytes;
    private long currentDocumenID;

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

            if (status == OperationStatus.SUCCESS && LongBinding.entryToLong(keyEntry) == termID) {
                TupleInput reader = new TupleInput(valueEntry.getData());
                reader.readLong();
                while (reader.available() > 0)
                    outputStream.write(reader.read());
            }

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
            currentDocumenID = posting.getDocumentID();
        }

        posting.writeDelimitedTo(outputStream);
    }

    private void flush() throws DatabaseException {
        TupleOutput tupleOutput = new TupleOutput();
        tupleOutput.writeLong(currentDocumenID);
        tupleOutput.writeFast(outputStream.toByteArray());
        valueEntry.setData(tupleOutput.toByteArray());
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
