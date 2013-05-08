package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.PostingsWriter;
import ru.chuprikov.search.database.datatypes.Datatypes;

import java.io.IOException;

class BerkeleyIndexDB extends ThreadLocalEntriesEntries implements IndexDB {
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
    public CloseableIterator<Datatypes.Posting> getPostingsList(long termID) throws Exception {
        return new BerkeleyPostingsIterator(indexDB, termID);
    }

    @Override
    public CloseableIterator<Datatypes.Posting> getPostingsList(long termID, long documentID) throws Exception {
        return new BerkeleyPostingsIterator(indexDB, termID, documentID);
    }

    @Override
    public void close() throws Exception {
        indexDB.close();
    }
}
