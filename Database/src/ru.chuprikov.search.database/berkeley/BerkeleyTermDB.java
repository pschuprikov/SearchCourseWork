package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.TermDB;

class BerkeleyTermDB implements TermDB {
    private final Database dictionaryDB;

    private final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<>();
    private final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<>();
    private final ThreadLocal<Sequence> idSequence = new ThreadLocal<>();

    BerkeleyTermDB(Environment env) throws Exception {
        keyEntry.set(new DatabaseEntry());
        valueEntry.set(new DatabaseEntry());

        DatabaseConfig dictionaryDatabaseConfig = new DatabaseConfig();
        dictionaryDatabaseConfig.setAllowCreate(true);
        dictionaryDB = env.openDatabase(null, "dictionary", dictionaryDatabaseConfig);

        SequenceConfig dictionaryIDSequenceConfig = new SequenceConfig();
        dictionaryIDSequenceConfig.setAllowCreate(true);
        IntegerBinding.intToEntry(0, keyEntry.get());
        idSequence.set(dictionaryDB.openSequence(null, keyEntry.get(), dictionaryIDSequenceConfig));
    }

    @Override
    public long getTermID(String term) throws Exception {
        return contains(term) ? LongBinding.entryToLong(valueEntry.get()) : -1;
    }

    @Override
    public long addTerm(String term) throws Exception {
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
    public void close() throws Exception {
        dictionaryDB.close();
    }
}
