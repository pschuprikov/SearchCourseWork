package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.Document;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebDocumentDB {
    @WebMethod
    Document getDocument(long documentID) throws Exception;

    @WebMethod
    Document getFirstDocument() throws Exception;

    @WebMethod
    Document[] getNextDocuments(long documentID, int length) throws Exception;
}
