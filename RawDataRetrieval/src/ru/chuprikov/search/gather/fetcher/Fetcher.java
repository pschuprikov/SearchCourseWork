package ru.chuprikov.search.gather.fetcher;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Fetcher {
    public <T> void fetchProblemAsync(FetchInfo<T> data, FetchCompletionHandler<T> handler);
    public void awaitCompletion() throws InterruptedException;
}
