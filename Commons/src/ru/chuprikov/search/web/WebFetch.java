package ru.chuprikov.search.web;


import ru.chuprikov.search.datatypes.ProblemID;
import ru.chuprikov.search.datatypes.ProblemRawData;
import ru.chuprikov.search.datatypes.ProcessStatistics;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebFetch {
    @WebMethod
    ProcessStatistics fetch(String resourceID, int from, int to) throws Exception;

    @WebMethod void clearFetches() throws Exception;

    @WebMethod
    ProblemRawData getProblemRawData(ProblemID problemID);
    @WebMethod ProblemRawData getFirstProblemRawData() throws Exception;
    @WebMethod ProblemRawData[] getNextProblemRawDatas(ProblemID problemID, int count) throws Exception;
}
