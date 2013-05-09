package ru.chuprikov.search.web;

import ru.chuprikov.search.database.datatypes.Document;
import ru.chuprikov.search.web.documents.WebDocumentDB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Documents implements Serializable {
    private Document[] lastDocuments;
    private final static QName qname = new QName("http://documents.web.search.chuprikov.ru/", "WebDocumentDBImplService");
    private final static int CHUNK_SIZE = 25;

    private WebDocumentDB webDocumentDB;

    @ManagedProperty(value="#{control}")
    private Control control;

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public Documents() throws Exception {
        try {
            URL url = new URL("http://localhost:8081/WS/documents?wsdl");
            webDocumentDB = Service.create(url, qname).getPort(WebDocumentDB.class);
            reset();
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        Document first = webDocumentDB.getFirstDocument();
        if (first == null)
            lastDocuments = new Document[0];
        else {
            lastDocuments = new Document[1];
            lastDocuments[0] = first;
        }
        advance();
    }

    public void resetWith() throws Exception {
        lastDocuments = webDocumentDB.getNextDocuments(requestDocumentID, CHUNK_SIZE);
    }

    public void advance() throws Exception {
        if (lastDocuments.length > 0) {
            lastDocuments = webDocumentDB.getNextDocuments(lastDocuments[lastDocuments.length - 1].getDocumentID(), CHUNK_SIZE);
        }
    }

    public String showContent() throws Exception {
        control.setActiveIdx(6);
        return "parsedProblem";
    }
}
