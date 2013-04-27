package ru.chuprikov.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FetchedDB extends AutoCloseable {
    void saveFetched(ProblemRawData problem) throws Exception;
    boolean contains(ProblemRawData problem) throws Exception;
}
