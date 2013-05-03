package ru.chuprikov.search.database;

public interface TermDB extends AutoCloseable {
    long getID(String term) throws Exception;
    boolean contains(String term) throws Exception;
    long add(String term) throws Exception;

    long size() throws Exception;
    CloseableIterator<String> iterator() throws Exception;
    CloseableIterator<String> upperBound(String first) throws Exception;
}
