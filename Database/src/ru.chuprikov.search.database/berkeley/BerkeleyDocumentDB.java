package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleTupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.StoredValueSet;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.DocumentDB;
import ru.chuprikov.search.database.datatypes.Document;
import ru.chuprikov.search.database.datatypes.ProblemID;

class BerkeleyDocumentDB extends ThreadLocalEntriesEntries implements DocumentDB {
    private final Database documentDB;
    private final Sequence documentIDSequence;
    private final StoredSortedMap<Long, Document> storedSortedMap;

    BerkeleyDocumentDB(Environment env) throws DatabaseException {
        DatabaseConfig documentDBConfig = new DatabaseConfig();
        documentDBConfig.setAllowCreate(true);
        documentDB = env.openDatabase(null, "document", documentDBConfig);

        storedSortedMap = new StoredSortedMap<>(documentDB, new LongBinding(), documentBinding, true);

        SequenceConfig documentIDSequenceConfig = new SequenceConfig();
        documentIDSequenceConfig.setInitialValue(1);
        documentIDSequenceConfig.setAllowCreate(true);
        documentIDSequenceConfig.setCacheSize(100);
        LongBinding.longToEntry(0, keyEntry.get());
        documentIDSequence = documentDB.openSequence(null, keyEntry.get(), documentIDSequenceConfig);
    }

    private static final TupleTupleBinding<Document> documentBinding = new TupleTupleBinding<Document>() {
        @Override
        public Document entryToObject(TupleInput keyInput, TupleInput dataInput) {
            return new Document(keyInput.readLong(), new ProblemID(dataInput.readString(), dataInput.readString()), dataInput.readString());
        }

        @Override
        public void objectToKey(Document object, TupleOutput output) {
            output.writeLong(object.getDocumentID());
        }

        @Override
        public void objectToData(Document object, TupleOutput output) {
            output.writeString(object.getProblemID().getResource()).writeString(object.getProblemID().getProblemID())
                .writeString(object.getUrl());
        }
    };

    @Override
    public long addDocument(Document document) throws Exception {
        final long documentID = documentIDSequence.get(null, 1);
        document.setDocumentID(documentID);
        storedSortedMap.put(documentID, document);
        return documentID;
    }

    @Override
    public Document get(long documentID) throws Exception {
        return storedSortedMap.get(documentID);
    }

    @Override
    public CloseableIterator<Document> iterator() throws Exception {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<Document>) storedSortedMap.values()).storedIterator());
    }

    @Override
    public CloseableIterator<Document> upperBound(long first) throws Exception {
        return new BerkeleyCloseableStoredIterator<>(((StoredValueSet<Document>) storedSortedMap.tailMap(first).values()).storedIterator());
    }

    @Override
    public void close() throws Exception {
        documentDB.close();
    }
}
