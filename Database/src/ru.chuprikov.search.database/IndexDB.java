package ru.chuprikov.search.database;

import javax.jws.WebService;

@WebService
public interface IndexDB extends AutoCloseable {
    PostingsWriter getPostingsWriter(long termID) throws Exception;
}
