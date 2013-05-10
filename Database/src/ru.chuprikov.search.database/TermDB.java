package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.Term;

public interface TermDB extends AutoCloseable {
    boolean contains(String term) throws Exception;
    long add(String term) throws Exception;
    void incrementCount(String term, long count) throws Exception;

    Term get(String term) throws Exception;
    CloseableIterator<String> iterator() throws Exception;
    CloseableIterator<String> upperBound(String first) throws Exception;
}
