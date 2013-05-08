package ru.chuprikov.search.database;

public interface TermDB extends AutoCloseable {
    boolean contains(String term) throws Exception;
    long add(String term) throws Exception;

    long get(String term) throws Exception;
    CloseableIterator<String> iterator() throws Exception;
    CloseableIterator<String> upperBound(String first) throws Exception;
}
