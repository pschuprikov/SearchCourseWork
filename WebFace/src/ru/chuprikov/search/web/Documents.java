package ru.chuprikov.search.web;

import ru.chuprikov.search.database.datatypes.Document;
import ru.chuprikov.search.web.documents.WebDocumentDB;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Documents {
    private Document[] lastDocuments;
    private final static QName qname = new QName("http://documents.web.search.chuprikov.ru/", "WebDocumentDBImplService");
    private final static int CHUNK_SIZE = 25;

    private WebDocumentDB webDocumentDB;

    @PostConstruct
    private void init() throws MalformedURLException {
        URL url = new URL("http://localhost:8081/WS/documents?wsdl");
        webDocumentDB = Service.create(url, qname).getPort(WebDocumentDB.class);
    }

    public Document[] getLastDocuments() {
        return lastDocuments;
    }

    public void setLastDocuments(Document[] lastDocuments) {
        this.lastDocuments = lastDocuments;
    }

    public long getRequestDocumentID() {
        return requestDocumentID;
    }

    public void setRequestDocumentID(long requestDocumentID) {
        this.requestDocumentID = requestDocumentID;
    }

    private long requestDocumentID;

    public void reset() throws Exception {
        Document first = webDocumentDB.getFirstDocumentInfo();
        if (first == null)
            lastDocuments = new Document[0];
        else {
            lastDocuments = new Document[1];
            lastDocuments[0] = first;
        }
        advance();
    }

    public void advance() throws Exception {
        if (lastDocuments.length > 0) {
            lastDocuments = webDocumentDB.getNextDocumentInfos(requestDocumentID, CHUNK_SIZE);
        }
    }
}
