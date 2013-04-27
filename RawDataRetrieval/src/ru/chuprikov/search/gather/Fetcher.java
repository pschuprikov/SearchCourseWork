package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */

interface Fetcher {
    void fetchProblemAsync(ProblemFetchData problem, FetchCompletionHandler handler);
    void awaitCompletion() throws InterruptedException;
}
