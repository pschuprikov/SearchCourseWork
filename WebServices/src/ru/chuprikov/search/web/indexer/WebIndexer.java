package ru.chuprikov.search.web.indexer;

import ru.chuprikov.search.database.datatypes.ProblemID;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebIndexer {
    @WebMethod
    void index(ProblemID from, ProblemID to, long maxMemoryUsage, int maxPostingsChunkSize) throws Exception;

    @WebMethod
    void indexAll(long maxMemoryUsage, int maxPostingsChunkSize) throws Exception;
}
