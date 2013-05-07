package ru.chuprikov.search.database;

import java.util.Iterator;

public interface TermDB extends AutoCloseable {
    boolean contains(String term) throws Exception;
    long add(String term) throws Exception;

    long get(String term) throws Exception;
    Iterator<String> iterator() throws Exception;
    Iterator<String> upperBound(String first) throws Exception;
}
