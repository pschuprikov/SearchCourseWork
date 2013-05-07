package ru.chuprikov.search.web.fetch;


import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebFetch {
    @WebMethod FetchStatistics fetch(String resourceID, int from, int to) throws Exception;

    @WebMethod void clearFetches() throws Exception;

    @WebMethod ProblemRawData getProblemRawData(ProblemID problemID);
    @WebMethod ProblemRawData getFirstProblemRawData() throws Exception;
    @WebMethod ProblemRawData[] getNextProblemRawDatas(ProblemID problemID, int count) throws Exception;
}
