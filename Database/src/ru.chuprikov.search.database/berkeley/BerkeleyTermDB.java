package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredKeySet;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.TermDB;

class BerkeleyTermDB extends AbstractBerkeleyDB implements TermDB {
    private final Database dictionaryDB;
    private final StoredSortedMap<String, Long> storedSortedMap;

    private final Sequence idSequence;

    BerkeleyTermDB(Environment env) {
        DatabaseConfig dictionaryDatabaseConfig = new DatabaseConfig();
        dictionaryDatabaseConfig.setAllowCreate(true);
        dictionaryDB = env.openDatabase(null, "term", dictionaryDatabaseConfig);

        storedSortedMap = new StoredSortedMap<>(dictionaryDB, new StringBinding(), new LongBinding(), false);

        SequenceConfig dictionaryIDSequenceConfig = new SequenceConfig();
        dictionaryIDSequenceConfig.setAllowCreate(true);
        dictionaryIDSequenceConfig.setInitialValue(1);
        IntegerBinding.intToEntry(0, keyEntry.get());
        idSequence = dictionaryDB.openSequence(null, keyEntry.get(), dictionaryIDSequenceConfig);
    }

    @Override
    public long get(String term) throws Exception {
        return storedSortedMap.get(term);
    }

    @Override
    public long add(String term) throws Exception {
        if (contains(term))
            return get(term);
        else {
            final long id = idSequence.get(null, 1);
            storedSortedMap.put(term, id);
            return id;
        }
    }

    @Override
    public boolean contains(String term) throws Exception {
        return storedSortedMap.containsKey(term);
    }

    @Override
    public CloseableIterator<String> iterator() throws Exception {
        return new BerkeleyCloseableStoredIterator<>(((StoredKeySet<String>) storedSortedMap.keySet()).storedIterator());
    }

    @Override
    public CloseableIterator<String> upperBound(String first) throws Exception {
        return new BerkeleyCloseableStoredIterator<>(((StoredKeySet<String>) storedSortedMap.tailMap(first).keySet()).storedIterator());
    }

    @Override
    public void close() throws Exception {
        idSequence.close();
        dictionaryDB.close();
    }
}
