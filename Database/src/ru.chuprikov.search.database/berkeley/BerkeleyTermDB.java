package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.TermDB;

import java.util.Iterator;

class BerkeleyTermDB extends AbstractBerkeleyDB implements TermDB {
    private final Database dictionaryDB;
    private final StoredSortedMap<String, Long> storedSortedMap;

    private final Sequence idSequence;

    BerkeleyTermDB(Environment env) throws Exception {
        DatabaseConfig dictionaryDatabaseConfig = new DatabaseConfig();
        dictionaryDatabaseConfig.setAllowCreate(true);
        dictionaryDB = env.openDatabase(null, "term", dictionaryDatabaseConfig);

        storedSortedMap = new StoredSortedMap<>(dictionaryDB, new StringBinding(), new LongBinding(), false);

        SequenceConfig dictionaryIDSequenceConfig = new SequenceConfig();
        dictionaryIDSequenceConfig.setAllowCreate(true);
        IntegerBinding.intToEntry(0, keyEntry.get());
        idSequence = dictionaryDB.openSequence(null, keyEntry.get(), dictionaryIDSequenceConfig);
    }

    @Override
    public long get(String term) throws Exception {
        return contains(term) ? LongBinding.entryToLong(valueEntry.get()) : -1;
    }

    @Override
    public long add(String term) throws Exception {
        if (contains(term))
            return LongBinding.entryToLong(valueEntry.get());
        else {
            StringBinding.stringToEntry(term, keyEntry.get());
            final long id = idSequence.get(null, 1);
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
    public Iterator<String> iterator() throws Exception {
        return storedSortedMap.keySet().iterator();
    }

    @Override
    public Iterator<String> upperBound(String first) throws Exception {
        return storedSortedMap.tailMap(first).keySet().iterator();
    }

    @Override
    public void close() throws Exception {
        idSequence.close();
        dictionaryDB.close();
    }
}
