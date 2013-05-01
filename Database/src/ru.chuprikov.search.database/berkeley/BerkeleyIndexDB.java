package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.*;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.PostingsWriter;

import java.io.IOException;

class BerkeleyIndexDB implements IndexDB {
    private final Database indexDB;

    private final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<>();
    private final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<>();

    private final int maxPostingsChunkSizeBytes;

    BerkeleyIndexDB(Environment env, int maxPostingsChunkSizeBytes) throws DatabaseException {
        keyEntry.set(new DatabaseEntry());
        valueEntry.set(new DatabaseEntry());

        this.maxPostingsChunkSizeBytes = maxPostingsChunkSizeBytes;

        DatabaseConfig indexDatabaseConfig = new DatabaseConfig();
        indexDatabaseConfig.setAllowCreate(true);
        indexDatabaseConfig.setSortedDuplicates(true);
        indexDB = env.openDatabase(null, "index", indexDatabaseConfig);
    }

    @Override
    public PostingsWriter getPostingsWriter(long termID) throws DatabaseException, IOException {
        return new BerkeleyPostingsWriter(indexDB, termID, maxPostingsChunkSizeBytes);
    }

    @Override
    public void close() throws Exception {
        indexDB.close();
    }
}
