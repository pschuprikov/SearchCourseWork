package ru.chuprikov.search.database;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleBinding;
import com.sleepycat.je.*;
import ru.kirillova.search.database.ParsedProblem;

import java.util.Iterator;

class BerkeleyParsedDB implements ParsedDB {
    private final Database db;

    BerkeleyParsedDB(Database db) {
        this.db = db;
    }

    @Override
    public Iterator<ParsedProblem> iterator() {
        try {
            return new ProblemDataIterator(this.db.openCursor(null, CursorConfig.DEFAULT));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ProblemDataIterator implements Iterator<ParsedProblem> {
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
        public ParsedProblem next() {
            try {
                if (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
                    keyEntry = null;
                    dataEntry = null;
                }
            } catch (DatabaseException e) {
                keyEntry = null;
                dataEntry = null;
            }

            return hasNext() ? binding.entryToObject(keyEntry, dataEntry) : null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class ProblemParseDataBinding extends TupleTupleBinding<ParsedProblem> {
        @Override
        public ParsedProblem entryToObject(TupleInput keyInput, TupleInput dataInput) {
            ParsedProblem result = new ParsedProblem(keyInput.readString(), keyInput.readString(), dataInput.readString());

            result.title = dataInput.readString();
            result.condition = dataInput.readString();
            result.inputSpecification = dataInput.readString();
            result.outputSpecification = dataInput.readString();

            return result;
        }

        @Override
        public void objectToKey(ParsedProblem object, TupleOutput output) {
            output.writeString(object.resource).writeString(object.problemID);
        }

        @Override
        public void objectToData(ParsedProblem object, TupleOutput output) {
            output.writeString(object.url).writeString(object.title).writeString(object.condition)
                    .writeString(object.inputSpecification).writeString(object.outputSpecification);
        }
    }

    private static final ProblemParseDataBinding binding = new ProblemParseDataBinding();

    @Override
    public void saveParsed(ParsedProblem problem) throws DatabaseException {
        final DatabaseEntry theKey = new DatabaseEntry();
        final DatabaseEntry theData = new DatabaseEntry();
        binding.objectToKey(problem, theKey);
        binding.objectToData(problem, theData);

        db.put(null, theKey, theData);
    }

    @Override
    public boolean contains(ParsedProblem problem) throws DatabaseException {
        final DatabaseEntry theKey = new DatabaseEntry();
        binding.objectToKey(problem, theKey);
        return db.get(null, theKey, new DatabaseEntry(), LockMode.DEFAULT) == OperationStatus.SUCCESS;
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
