package ru.chuprikov.search.web;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface WebDocumentDB {
    @WebMethod DocumentInfo getDocumentInfo(long id);
    @WebMethod DocumentInfo getFirstDocumentInfo();
    @WebMethod DocumentInfo[] getNextDocumentInfos(DocumentInfo termInfo, int length);
}
