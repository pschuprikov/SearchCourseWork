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
import ru.chuprikov.search.database.ParsedDB;
import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemID;

class BerkeleyParsedDB implements ParsedDB {
    private final Database db;
    private final StoredSortedMap<ProblemID, ParsedProblem> storedMap;

    BerkeleyParsedDB(Environment env) throws DatabaseException {
        DatabaseConfig dbConf = new DatabaseConfig();
        dbConf.setAllowCreate(true);
        this.db = env.openDatabase(null, "parsed", dbConf);
        storedMap = new StoredSortedMap<>(db, problemIDBinding, problemParseDataBinding, true);
    }

    @Override
    public CloseableListIterator<ParsedProblem> iterator() {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<ParsedProblem>)storedMap.values()).storedIterator());
    }

    @Override
    public CloseableListIterator<ParsedProblem> upperBound(ProblemID problemID) {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<ParsedProblem>)storedMap.tailMap(problemID).values()).storedIterator());
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

    private static final TupleTupleBinding<ParsedProblem> problemParseDataBinding = new TupleTupleBinding<ParsedProblem>() {
        @Override
        public ParsedProblem entryToObject(TupleInput keyInput, TupleInput dataInput) {
            ParsedProblem result = new ParsedProblem(problemIDBinding.entryToObject(keyInput), dataInput.readString());

            result.setTitle(dataInput.readString());
            result.setCondition(dataInput.readString());
            result.setInputSpecification(dataInput.readString());
            result.setOutputSpecification(dataInput.readString());

            return result;
        }

        @Override
        public void objectToKey(ParsedProblem object, TupleOutput output) {
            problemIDBinding.objectToEntry(object.getProblemID(), output);
        }

        @Override
        public void objectToData(ParsedProblem object, TupleOutput output) {
            output.writeString(object.getUrl()).writeString(object.getTitle()).writeString(object.getCondition())
                .writeString(object.getInputSpecification()).writeString(object.getOutputSpecification());
        }
    };

    @Override
    public void saveParsed(ParsedProblem problem) throws DatabaseException {
        storedMap.put(problem.getProblemID(), problem);
    }

    @Override
    public boolean contains(ProblemID problemID) throws DatabaseException {
        return storedMap.containsKey(problemID);
    }

    @Override
    public ParsedProblem get(ProblemID problemID) {
        return storedMap.get(problemID);
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
