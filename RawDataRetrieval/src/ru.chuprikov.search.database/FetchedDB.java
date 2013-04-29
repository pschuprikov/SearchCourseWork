package ru.chuprikov.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

public interface FetchedDB extends AutoCloseable {
    void saveFetched(ProblemRawData problem) throws Exception;
    boolean contains(ProblemRawData problem) throws Exception;
    CloseableIterator<ProblemRawData> openIterator();
}
