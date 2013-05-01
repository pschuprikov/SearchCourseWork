package ru.chuprikov.search.database;

public interface TermDB extends AutoCloseable {
    long getTermID(String term) throws Exception;
    long addTerm(String term) throws Exception;
    boolean contains(String term) throws Exception;
}
