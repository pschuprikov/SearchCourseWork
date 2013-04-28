package ru.chuprikov.search.gather.fetcher;

public interface FetchInfo <T> {
    URLContentLoader<T> getLoader();
    T get();
    String getURL();
}
