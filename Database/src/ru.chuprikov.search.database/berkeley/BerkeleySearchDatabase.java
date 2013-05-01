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
    public TermDB openTermDB() throws Exception {
        return new BerkeleyTermDB(env);
    }

    @Override
    public void close() throws Exception {
        env.close();
    }
}
