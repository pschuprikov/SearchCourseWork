package ru.chuprikov.search.gather.fetcher;

import java.io.IOException;
import java.net.Proxy;

public interface URLContentLoader<T> {
    public String loadContent(FetchInfo<T> data, Proxy proxy) throws IOException;
}
