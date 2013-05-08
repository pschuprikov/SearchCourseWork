package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.*;
import ru.chuprikov.search.database.*;

import java.io.File;

public class BerkeleySearchDatabase implements SearchDatabase {
    private final Environment env;
    private final Database sequencesDB;

    public BerkeleySearchDatabase(File file) throws DatabaseException {
        EnvironmentConfig conf = new EnvironmentConfig();
        conf.setAllowCreate(true);
        env = new Environment(file, conf);

        sequencesDB = env.openDatabase(null, "seqs", DatabaseConfig.DEFAULT.setAllowCreate(true));
    }

    @Override
    public FetchedDB openFetchDB() throws DatabaseException {
        return new BerkeleyFetchedDB(env);
    }

    @Override
    public void truncateFetchDB() throws Exception {
        env.truncateDatabase(null, "fetched", false);
    }

    @Override
    public ParsedDB openParseDB() throws Exception {
        return new BerkeleyParsedDB(env);
    }

    @Override
    public void truncateParseDB() throws Exception {
        env.truncateDatabase(null, "parsed", false);
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
        return new BerkeleyTermDB(env, sequencesDB);
    }

    @Override
    public void truncateTermDB() throws Exception {
        env.truncateDatabase(null, "term", false);
    }

    @Override
    public DocumentDB openDocumentDB() throws Exception {
        return new BerkeleyDocumentDB(env, sequencesDB);
    }

    @Override
    public void truncateDocumentDB() throws Exception {
        env.truncateDatabase(null, "document", false);
    }

    @Override
    public void close() throws Exception {
        sequencesDB.close();
        env.close();
    }
}
