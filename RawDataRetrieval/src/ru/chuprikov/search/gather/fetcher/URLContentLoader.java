package ru.chuprikov.search.gather.fetcher;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface URLContentLoader <T> {
    public String loadContent(T problem, Proxy proxy) throws IOException;
}
