package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.je.*;
import ru.chuprikov.search.database.DocumentDB;
import ru.chuprikov.search.database.datatypes.Datatypes;

public class BerkeleyDocumentDB implements DocumentDB {
    private final Database documentDB;
    private final Sequence documentIDSequence;

    private final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<>();
    private final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<>();

    BerkeleyDocumentDB(Environment env) throws DatabaseException {
        keyEntry.set(new DatabaseEntry());
        valueEntry.set(new DatabaseEntry());

        DatabaseConfig documentDBConfig = new DatabaseConfig();
        documentDBConfig.setAllowCreate(true);
        documentDB = env.openDatabase(null, "document", documentDBConfig);

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
        if (documentDB.get(null, keyEntry.get(), valueEntry.get(), LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            return Datatypes.Document.parseFrom(valueEntry.get().getData());
        } else {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        documentDB.close();
    }
}
