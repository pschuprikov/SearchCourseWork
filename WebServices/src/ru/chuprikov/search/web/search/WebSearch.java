package ru.chuprikov.search.web.search;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebSearch {
    @WebMethod
    Long[] searchSimpleConjunction(String request, int limit) throws Exception;
}
