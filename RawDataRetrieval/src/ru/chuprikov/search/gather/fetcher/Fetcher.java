package ru.chuprikov.search.gather.fetcher;

public interface Fetcher {
    public <T> void fetchProblemAsync(FetchInfo<T> data, FetchCompletionHandler<T> handler);
    public void awaitCompletion() throws InterruptedException;
}
