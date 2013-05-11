package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.ProblemID;
import ru.chuprikov.search.datatypes.ProcessStatistics;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebIndexer {
    @WebMethod
    ProcessStatistics index(ProblemID from, ProblemID to, long maxMemoryUsage, int maxPostingsChunkSize) throws Exception;

    @WebMethod
    ProcessStatistics indexAll(long maxMemoryUsage, int maxPostingsChunkSize) throws Exception;
}
