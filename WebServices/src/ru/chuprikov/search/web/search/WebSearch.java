package ru.chuprikov.search.web.search;

import ru.chuprikov.search.database.datatypes.Document;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebSearch {
    @WebMethod
    Document[] searchSimpleConjunction(String request, int limit) throws Exception;
}
