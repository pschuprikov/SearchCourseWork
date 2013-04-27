package ru.chuprikov.search.database;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchDatabases {
    public static SearchDatabase openBerkeley(File filename) throws Exception{
        return new BerkleySearchDatabse(filename);
    }
}
