package ru.chuprikov.search.web.terms;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebTermDB {

    @WebMethod
    TermInfo getTermInfo(String term) throws Exception;

    @WebMethod
    TermInfo getFirstTermInfo() throws Exception;

    @WebMethod
    TermInfo[] getNextTermInfos(String term, int length);
}
