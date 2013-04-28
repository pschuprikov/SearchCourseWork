package ru.chuprikov.search.database;

import java.io.File;

public class SearchDatabases {
    public static SearchDatabase openBerkeley(File filename) throws Exception{
        return new BerkeleySearchDatabase(filename);
    }
}
