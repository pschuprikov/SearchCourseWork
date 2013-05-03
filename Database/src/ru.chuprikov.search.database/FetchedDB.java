package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.ProblemRawData;

import javax.jws.WebService;

@WebService
public interface FetchedDB extends AutoCloseable {
    void saveFetched(ProblemRawData problem) throws Exception;
    boolean contains(ProblemRawData problem) throws Exception;
    CloseableIterator<ProblemRawData> openIterator();
}
