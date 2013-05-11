package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.PostingInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebIndexDB {
    @WebMethod
    PostingInfo readFirstPosting(long termID) throws Exception;

    @WebMethod
    PostingInfo[] readNextPostings(long termID, long documentID, int count) throws Exception;
}
