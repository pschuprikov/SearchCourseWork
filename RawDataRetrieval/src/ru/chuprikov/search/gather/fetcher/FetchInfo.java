package ru.chuprikov.search.gather.fetcher;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FetchInfo <T> {
    URLContentLoader<T> getLoader();
    T get();
}
