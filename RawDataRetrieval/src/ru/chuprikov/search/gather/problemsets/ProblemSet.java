package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.URLContentLoader;

import java.util.Iterator;

public interface ProblemSet extends Iterator<ProblemFetchInfo> {
    boolean checkValid(String content);
    URLContentLoader<ProblemFetchInfo> getLoader();
}
