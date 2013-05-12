package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.SearchResponse;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebSearch {
    @WebMethod
    SearchResponse searchSimpleConjunction(String request, int limit) throws Exception;
}
