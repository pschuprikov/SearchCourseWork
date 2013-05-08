package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.PostingsWriter;

import java.io.IOException;

class BerkeleyIndexDB extends AbstractBerkeleyDB implements IndexDB {
    private final Database indexDB;

    private final int maxPostingsChunkSizeBytes;

    BerkeleyIndexDB(Environment env, int maxPostingsChunkSizeBytes) throws DatabaseException {
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
