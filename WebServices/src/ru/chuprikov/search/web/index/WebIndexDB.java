package ru.chuprikov.search.web.index;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebIndexDB {
    @WebMethod
    byte[] readFirstPosting(long termID) throws Exception;

    @WebMethod
    byte[][] readNextPostings(long termID, long documentID, int count) throws Exception;
}
