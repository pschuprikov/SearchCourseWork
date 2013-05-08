package ru.chuprikov.search.web.fetch;

import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemID;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebParse {
    @WebMethod ProcessStatistics parse(ProblemID from, ProblemID to) throws Exception;
    @WebMethod ProcessStatistics parseAll() throws Exception;

    @WebMethod void clearParsed() throws Exception;

    @WebMethod
    ParsedProblem getProblemParsed(ProblemID problemID);
    @WebMethod ParsedProblem getFirstProblemParsed() throws Exception;
    @WebMethod ParsedProblem[] getNextProblemParseds(ProblemID problemID, int count) throws Exception;
}
