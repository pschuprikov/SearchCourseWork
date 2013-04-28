package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.URLContentLoader;
import ru.chuprikov.search.gather.loader.ProblemFetchInfo;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProblemSet extends Iterator<ProblemFetchInfo> {
    boolean checkValid(String content);
    URLContentLoader<ProblemFetchInfo> getLoader();
}
