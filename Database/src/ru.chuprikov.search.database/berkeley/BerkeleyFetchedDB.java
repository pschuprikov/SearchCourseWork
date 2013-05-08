package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.StoredValueSet;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import ru.chuprikov.search.database.CloseableListIterator;
import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

class BerkeleyFetchedDB implements FetchedDB {
    private final Database db;
    private final StoredSortedMap<ProblemID, ProblemRawData> storedMap;

    BerkeleyFetchedDB(Environment env) throws DatabaseException {
        DatabaseConfig dbConf = new DatabaseConfig();
        dbConf.setAllowCreate(true);
        this.db = env.openDatabase(null, "fetched", dbConf);
        storedMap = new StoredSortedMap<>(db, problemIDBinding, problemRawDataBinding, true);
    }

    @Override
    public CloseableListIterator<ProblemRawData> iterator() {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<ProblemRawData>) storedMap.values()).storedIterator());
    }

    @Override
    public CloseableListIterator<ProblemRawData> upperBound(ProblemID problemID) {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<ProblemRawData>) storedMap.tailMap(problemID).values()).storedIterator());
    }

    private static final TupleBinding<ProblemID> problemIDBinding = new TupleBinding<ProblemID>() {
        @Override
        public ProblemID entryToObject(TupleInput input) {
            return new ProblemID(input.readString(), input.readString());
        }

        @Override
        public void objectToEntry(ProblemID object, TupleOutput output) {
            output.writeString(object.getResource()).writeString(object.getProblemID());
        }
    };

    private static final TupleTupleBinding<ProblemRawData> problemRawDataBinding = new  TupleTupleBinding<ProblemRawData>() {

        @Override
        public ProblemRawData entryToObject(TupleInput keyInput, TupleInput dataInput) {
            return new ProblemRawData(problemIDBinding.entryToObject(keyInput), dataInput.readString(), dataInput.readString());
        }

        @Override
        public void objectToKey(ProblemRawData object, TupleOutput output) {
            problemIDBinding.objectToEntry(object.getProblemID(), output);
        }

        @Override
        public void objectToData(ProblemRawData object, TupleOutput output) {
            output.writeString(object.getUrl()).writeString(object.getContent());
        }
    };

    @Override
    public void saveFetched(ProblemRawData problem) {
        storedMap.put(problem.getProblemID(), problem);
    }

    @Override
    public ProblemRawData get(ProblemID problemID) {
        return storedMap.get(problemID);
    }

    @Override
    public boolean contains(ProblemID problemID) {
        return storedMap.containsKey(problemID);
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
