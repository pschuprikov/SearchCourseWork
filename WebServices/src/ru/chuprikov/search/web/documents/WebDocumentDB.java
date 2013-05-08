package ru.chuprikov.search.web.documents;

import ru.chuprikov.search.database.datatypes.Document;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebDocumentDB {
    @WebMethod
    Document getDocumentInfo(long documentID) throws Exception;

    @WebMethod
    Document getFirstDocumentInfo() throws Exception;

    @WebMethod
    Document[] getNextDocumentInfos(long documentID, int length) throws Exception;
}
