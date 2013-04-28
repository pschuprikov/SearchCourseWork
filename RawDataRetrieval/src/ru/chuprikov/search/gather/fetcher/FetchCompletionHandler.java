package ru.chuprikov.search.gather.fetcher;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */

public interface FetchCompletionHandler<T> {
    void handleFetchCompletion(T data, String content);

    void fetchFailed(T data, Exception reason);
}
