package ru.chuprikov.search.database;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import java.io.File;

class BerkeleySearchDatabase implements SearchDatabase {
    private final Environment env;

    BerkeleySearchDatabase(File file) throws DatabaseException {
        EnvironmentConfig conf = new EnvironmentConfig();
        conf.setAllowCreate(true);
        env = new Environment(file, conf);
    }

    @Override
    public FetchedDB openFetchDB() throws DatabaseException {
        DatabaseConfig dbConf = new DatabaseConfig();
        dbConf.setAllowCreate(true);
        return new BerkeleyFetchedDB(env.openDatabase(null, "fetched", dbConf));
    }

    @Override
    public ParsedDB openParseDB() throws Exception {
        DatabaseConfig dbConf = new DatabaseConfig();
        dbConf.setAllowCreate(true);
        return new BerkeleyParsedDB(env.openDatabase(null, "parsed", dbConf));
    }

    @Override
    public void close() throws Exception {
        env.close();
    }
}
