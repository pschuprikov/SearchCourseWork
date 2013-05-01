package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

class BerkeleyFetchedDB implements FetchedDB {
    private final Database db;

    BerkeleyFetchedDB(Environment env) throws DatabaseException {
        DatabaseConfig dbConf = new DatabaseConfig();
        dbConf.setAllowCreate(true);
        this.db = env.openDatabase(null, "fetched", dbConf);
    }

    @Override
    public CloseableIterator<ProblemRawData> openIterator() {
        try {
            return new ProblemDataIterator(this.db.openCursor(null, CursorConfig.DEFAULT));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ProblemDataIterator implements CloseableIterator<ProblemRawData> {
        private final Cursor cursor;
        private DatabaseEntry keyEntry = new DatabaseEntry();
        private DatabaseEntry dataEntry = new DatabaseEntry();

        ProblemDataIterator(Cursor cursor) throws DatabaseException {
            this.cursor = cursor;
            if (cursor.getFirst(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                keyEntry = null;
                dataEntry = null;
            }
        }

        @Override
        public boolean hasNext() {
            return keyEntry != null;
        }

        @Override
        public ProblemRawData next() {
            if (hasNext()) {
                ProblemRawData res = binding.entryToObject(keyEntry, dataEntry);
                advance();
                return res;
            } else {
                return null;
            }
        }

        private void advance() {
            try {
                if (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                    keyEntry = null;
                    dataEntry = null;
                }
            } catch (DatabaseException e) {
                keyEntry = null;
                dataEntry = null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws Exception {
            cursor.close();
        }
    }

    private static class ProblemFetchDataBinding extends TupleTupleBinding<ProblemRawData> {

        @Override
        public ProblemRawData entryToObject(TupleInput keyInput, TupleInput dataInput) {
            return new ProblemRawData(keyInput.readString(), keyInput.readString(), dataInput.readString(),
                dataInput.readString());
        }

        @Override
        public void objectToKey(ProblemRawData object, TupleOutput output) {
            output.writeString(object.resource).writeString(object.problemID);
        }

        @Override
        public void objectToData(ProblemRawData object, TupleOutput output) {
            output.writeString(object.url).writeString(object.content);
        }
    }

    private static final ProblemFetchDataBinding binding = new ProblemFetchDataBinding();

    @Override
    public void saveFetched(ProblemRawData problem) throws DatabaseException {
        final DatabaseEntry theKey = new DatabaseEntry();
        final DatabaseEntry theData = new DatabaseEntry();
        binding.objectToKey(problem, theKey);
        binding.objectToData(problem, theData);

        db.put(null, theKey, theData);
    }

    @Override
    public boolean contains(ProblemRawData problem) throws DatabaseException {
        final DatabaseEntry theKey = new DatabaseEntry();
        binding.objectToKey(problem, theKey);
        return db.get(null, theKey, new DatabaseEntry(), LockMode.DEFAULT) == OperationStatus.SUCCESS;
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
