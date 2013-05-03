package ru.chuprikov.search.database.web;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebSearchDB {
    @WebMethod long size();
    @WebMethod TermInfo getTermInfo(String term);
    @WebMethod TermInfo getFirstTermInfo();
    @WebMethod TermInfo[] getNextTermInfos(String str, int length);
}
