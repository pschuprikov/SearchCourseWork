package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemID;
import ru.chuprikov.search.datatypes.ProcessStatistics;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebParse {
    @WebMethod
    ProcessStatistics parse(ProblemID from, ProblemID to) throws Exception;

    @WebMethod
    ProcessStatistics parseAll() throws Exception;

    @WebMethod
    void clearParsed() throws Exception;

    @WebMethod
    ParsedProblem getProblemParsed(ProblemID problemID);

    @WebMethod
    ParsedProblem getFirstProblemParsed() throws Exception;

    @WebMethod
    ParsedProblem[] getNextProblemParseds(ProblemID problemID, int count) throws Exception;
}
