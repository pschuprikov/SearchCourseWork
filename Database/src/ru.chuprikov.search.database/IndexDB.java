package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.Datatypes;

import javax.jws.WebService;

@WebService
public interface IndexDB extends AutoCloseable {
    PostingsWriter getPostingsWriter(long termID) throws Exception;
    CloseableIterator<Datatypes.Posting> getPostingsList(long termID) throws Exception;
    CloseableIterator<Datatypes.Posting> getPostingsList(long termID, long documentID) throws Exception;
}
