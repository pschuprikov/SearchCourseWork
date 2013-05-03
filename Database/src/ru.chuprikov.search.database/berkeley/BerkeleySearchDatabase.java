package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import ru.chuprikov.search.database.*;

import java.io.File;

public class BerkeleySearchDatabase implements SearchDatabase {
    private final Environment env;

    public BerkeleySearchDatabase(File file) throws DatabaseException {
        EnvironmentConfig conf = new EnvironmentConfig();
        conf.setAllowCreate(true);
        env = new Environment(file, conf);
    }

    @Override
    public FetchedDB openFetchDB() throws DatabaseException {
        return new BerkeleyFetchedDB(env);
    }

    @Override
    public ParsedDB openParseDB() throws Exception {
        return new BerkeleyParsedDB(env);
    }

    @Override
    public IndexDB openIndexDB(int maxPostingsChunkSizeBytes) throws Exception {
        return new BerkeleyIndexDB(env, maxPostingsChunkSizeBytes);
    }

    @Override
    public void truncateIndexDB() throws Exception {
        env.truncateDatabase(null, "index", false);
    }

    @Override
    public TermDB openTermDB() throws Exception {
        return new BerkeleyTermDB(env);
    }

    @Override
    public void truncateTermDB() throws Exception {
        env.truncateDatabase(null, "term", false);
    }

    @Override
    public DocumentDB openDocumentDB() throws Exception {
        return new BerkeleyDocumentDB(env);
    }

    @Override
    public void truncateDocumentDB() throws Exception {
        env.truncateDatabase(null, "document", false);
    }

    @Override
    public void close() throws Exception {
        env.close();
    }
}
