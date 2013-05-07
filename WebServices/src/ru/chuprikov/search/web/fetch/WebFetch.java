package ru.chuprikov.search.web.fetch;


import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebFetch {
    @WebMethod
    FetchStatistics fetch(String resourceID, int from, int to) throws Exception;


}
