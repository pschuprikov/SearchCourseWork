package ru.chuprikov.search.database;

import ru.chuprikov.search.database.berkeley.BerkeleySearchDatabase;

import java.io.File;

public class SearchDatabases {
    public static SearchDatabase openBerkeley(File filename) throws Exception{
        return new BerkeleySearchDatabase(filename);
    }
}
