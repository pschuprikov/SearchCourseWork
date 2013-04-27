package ru.chuprikov.search.database;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.gather.ProblemRawData;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
class BerkeleyFetchedDB implements FetchedDB {
    private final Database db;

    BerkeleyFetchedDB(Database db) {
        this.db = db;
    }

    private static class ProblemFetchDataBinding extends TupleTupleBinding<ProblemRawData> {

        @Override
        public ProblemRawData entryToObject(TupleInput keyInput, TupleInput dataInput) {
            String resource = keyInput.readString();
            String problemID = keyInput.readString();
            String url = dataInput.readString();
            String content = dataInput.readString();
            return new ProblemRawData(resource, problemID, url, content);
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

    private static ProblemFetchDataBinding binding = new ProblemFetchDataBinding();

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
