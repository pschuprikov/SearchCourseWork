package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.*;
import com.sleepycat.collections.StoredKeySet;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.TermDB;
import ru.chuprikov.search.datatypes.Term;

class BerkeleyTermDB extends ThreadLocalEntriesEntries implements TermDB {
    private final Database dictionaryDB;
    private final SecondaryDatabase termByIDDB;
    private final StoredSortedMap<String, Term> storedSortedMap;
    private final StoredSortedMap<Long, Term> storedSortedIndexMap;

    private final Sequence idSequence;

    static final String TERM_DB = "term";
    static final String TERM_BY_ID_IDX ="term_by_id_idx";

    private final SecondaryKeyCreator idByTermKeyCreator = new TupleTupleKeyCreator<Long>() {
        @Override
        public boolean createSecondaryKey(TupleInput primaryKeyInput, TupleInput dataInput, TupleOutput indexKeyOutput) {
            indexKeyOutput.writeLong(dataInput.readLong());
            return true;
        }
    };

    BerkeleyTermDB(Environment env, Database sequencesDB) {
        dictionaryDB = env.openDatabase(null, TERM_DB, new DatabaseConfig().setAllowCreate(true));
        termByIDDB = env.openSecondaryDatabase(null, TERM_BY_ID_IDX, dictionaryDB, (SecondaryConfig)
                new SecondaryConfig().setAllowPopulate(true).setKeyCreator(idByTermKeyCreator).setAllowCreate(true));

        storedSortedMap = new StoredSortedMap<>(dictionaryDB, new StringBinding(), termEntityBinding, true);
        storedSortedIndexMap = new StoredSortedMap<>(termByIDDB, new LongBinding(), termEntityBinding, true);

        StringBinding.stringToEntry(TERM_DB, keyEntry.get());
        idSequence = sequencesDB.openSequence(null, keyEntry.get(), SequenceConfig.DEFAULT.setAllowCreate(true).setInitialValue(1));
    }

    private static TupleTupleBinding<Term> termEntityBinding = new TupleTupleBinding<Term>() {
        @Override
        public Term entryToObject(TupleInput keyInput, TupleInput dataInput) {
            return new Term(keyInput.readString(), dataInput.readLong(), dataInput.readLong());
        }

        @Override
        public void objectToKey(Term object, TupleOutput output) {
            output.writeString(object.getTerm());
        }

        @Override
        public void objectToData(Term object, TupleOutput output) {
            output.writeLong(object.getTermID()).writeLong(object.getCount());
        }
    };

    @Override
    public Term get(String term) throws Exception {
        return storedSortedMap.get(term);
    }

    @Override
    public Term get(long termID) throws Exception {
        return storedSortedIndexMap.get(termID);
    }

    @Override
    public long add(final String term) throws Exception {
        if (contains(term)) {
            return get(term).getTermID();
        }
        else {
            final long id = idSequence.get(null, 1);
            storedSortedMap.put(term, new Term(term, id, 0));
            return id;
        }
    }

    @Override
    public void incrementCount(String term, long count) throws Exception {
        Term oldTerm = get(term);
        storedSortedMap.put(oldTerm.getTerm(), new Term(oldTerm.getTerm(), oldTerm.getTermID(), oldTerm.getCount() + count));
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
        termByIDDB.close();
        dictionaryDB.close();
    }
}
