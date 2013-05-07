package ru.chuprikov.search.web.terms;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebTermDB {
    @WebMethod TermInfo getTermInfo(String term);
    @WebMethod TermInfo getFirstTermInfo();
    @WebMethod TermInfo[] getNextTermInfos(String term, int length);
}
