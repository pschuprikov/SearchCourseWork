package ru.chuprikov.search.gather.fetcher;

public interface FetchCompletionHandler<T> {
    void handleFetchCompletion(T data, String content);

    void fetchFailed(T data, Exception reason);
}
