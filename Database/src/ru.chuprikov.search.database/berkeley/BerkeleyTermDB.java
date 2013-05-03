package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.TermDB;

class BerkeleyTermDB implements TermDB {
    private final Database dictionaryDB;

    private final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
    private final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
    private final ThreadLocal<Sequence> idSequence = new ThreadLocal<>();

    BerkeleyTermDB(Environment env) throws Exception {
        DatabaseConfig dictionaryDatabaseConfig = new DatabaseConfig();
        dictionaryDatabaseConfig.setAllowCreate(true);
        dictionaryDB = env.openDatabase(null, "term", dictionaryDatabaseConfig);

        SequenceConfig dictionaryIDSequenceConfig = new SequenceConfig();
        dictionaryIDSequenceConfig.setAllowCreate(true);
        IntegerBinding.intToEntry(0, keyEntry.get());
        idSequence.set(dictionaryDB.openSequence(null, keyEntry.get(), dictionaryIDSequenceConfig));
    }

    @Override
    public long getID(String term) throws Exception {
        return contains(term) ? LongBinding.entryToLong(valueEntry.get()) : -1;
    }

    @Override
    public long add(String term) throws Exception {
        if (contains(term))
            return LongBinding.entryToLong(valueEntry.get());
        else {
            StringBinding.stringToEntry(term, keyEntry.get());
            final long id = idSequence.get().get(null, 1);
            LongBinding.longToEntry(id, valueEntry.get());
            dictionaryDB.put(null, keyEntry.get(), valueEntry.get());
            return id;
        }
    }

    @Override
    public boolean contains(String term) throws Exception {
        StringBinding.stringToEntry(term, keyEntry.get());
        return dictionaryDB.get(null, keyEntry.get(), valueEntry.get(), LockMode.DEFAULT) == OperationStatus.SUCCESS;
    }

    @Override
    public long size() throws Exception {
        return dictionaryDB.count();
    }

    @Override
    public CloseableIterator<String> iterator() throws Exception {
        return new TermCloseableIterator(dictionaryDB);
    }

    private class TermCloseableIterator implements CloseableIterator<String> {
        private Cursor cursor;
        private DatabaseEntry keyEntry = new DatabaseEntry();
        private DatabaseEntry valueEntry = new DatabaseEntry();

        TermCloseableIterator(Database db) throws DatabaseException {

            cursor = db.openCursor(null, CursorConfig.DEFAULT);
            if (cursor.getFirst(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                keyEntry = null;
                valueEntry = null;
            }
        }

        TermCloseableIterator(Database db, String first) throws DatabaseException {
            keyEntry = new DatabaseEntry();
            valueEntry = new DatabaseEntry();

            cursor = db.openCursor(null, CursorConfig.DEFAULT);
            StringBinding.stringToEntry(first, keyEntry);
            if (cursor.getSearchKey(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                keyEntry = null;
                valueEntry = null;
            }
        }

        @Override
        public void close() throws Exception {
            cursor.close();
        }

        @Override
        public boolean hasNext() {
            return keyEntry != null;
        }

        @Override
        public String next() {
            String result = StringBinding.entryToString(keyEntry);
            advance();
            return result;
        }

        private void advance() {
            if (hasNext()) {
                try {
                    if (cursor.getNext(keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                        keyEntry = null;
                        valueEntry = null;
                    }
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public CloseableIterator<String> upperBound(String first) throws Exception {
        return new TermCloseableIterator(dictionaryDB, first);
    }

    @Override
    public void close() throws Exception {
        dictionaryDB.close();
    }
}
