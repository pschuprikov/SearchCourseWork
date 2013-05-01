package ru.chuprikov.search.database;

public interface IndexDB extends AutoCloseable {
    PostingsWriter getPostingsWriter(long termID) throws Exception;
}
