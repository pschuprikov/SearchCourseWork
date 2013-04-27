package ru.chuprikov.search.database;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchDatabase extends AutoCloseable {
    FetchedDB openFetchDB() throws Exception;
}
