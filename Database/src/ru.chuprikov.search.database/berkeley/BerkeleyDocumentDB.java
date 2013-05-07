package ru.chuprikov.search.database.berkeley;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.DocumentDB;
import ru.chuprikov.search.database.datatypes.Datatypes;

import java.util.Iterator;

public class BerkeleyDocumentDB extends AbstractBerkeleyDB implements DocumentDB {
    private final Database documentDB;
    private final Sequence documentIDSequence;
    private final StoredSortedMap<Long, Datatypes.Document> storedSortedMap;

    BerkeleyDocumentDB(Environment env) throws DatabaseException {
        DatabaseConfig documentDBConfig = new DatabaseConfig();
        documentDBConfig.setAllowCreate(true);
        documentDB = env.openDatabase(null, "document", documentDBConfig);

        storedSortedMap = new StoredSortedMap<>(documentDB, new LongBinding(), new EntryBinding<Datatypes.Document>() {
            @Override
            public Datatypes.Document entryToObject(DatabaseEntry databaseEntry) {
                try {
                    Datatypes.Document.parseFrom(databaseEntry.getData());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void objectToEntry(Datatypes.Document document, DatabaseEntry databaseEntry) {
                databaseEntry.setData(document.toByteArray());
            }
        }, false);

        SequenceConfig documentIDSequenceConfig = new SequenceConfig();
        documentIDSequenceConfig.setInitialValue(1);
        documentIDSequenceConfig.setAllowCreate(true);
        documentIDSequenceConfig.setCacheSize(100);
        LongBinding.longToEntry(0, keyEntry.get());
        documentIDSequence = documentDB.openSequence(null, keyEntry.get(), documentIDSequenceConfig);
    }

    @Override
    public long addDocument(Datatypes.Document document) throws Exception {
        final long documentID = documentIDSequence.get(null, 1);
        LongBinding.longToEntry(documentID, keyEntry.get());
        valueEntry.get().setData(document.toByteArray());
        documentDB.put(null, keyEntry.get(), valueEntry.get());
        return documentID;
    }

    @Override
    public Datatypes.Document getDocument(long documentID) throws Exception {
        LongBinding.longToEntry(documentID, keyEntry.get());
        if (documentDB.get(null, keyEntry.get(), valueEntry.get(), LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            return Datatypes.Document.parseFrom(valueEntry.get().getData());
        } else {
            return null;
        }
    }

    @Override
    public Iterator<Long> iterator() throws Exception {
        return storedSortedMap.keySet().iterator();
    }

    @Override
    public Iterator<Long> upperBound(long first) throws Exception {
        return storedSortedMap.tailMap(first).keySet().iterator();
    }

    @Override
    public void close() throws Exception {
        documentDB.close();
    }
}
