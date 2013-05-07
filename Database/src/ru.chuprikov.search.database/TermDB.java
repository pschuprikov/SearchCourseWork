package ru.chuprikov.search.database;

import java.util.Iterator;

public interface TermDB extends AutoCloseable {
    long getID(String term) throws Exception;
    boolean contains(String term) throws Exception;
    long add(String term) throws Exception;

    Iterator<String> iterator() throws Exception;
    Iterator<String> upperBound(String first) throws Exception;
}
