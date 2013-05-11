package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.PostingInfo;

import javax.annotation.PostConstruct;
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
public class Postings implements Serializable {
    private PostingInfo[] lastInfos;
    private static final int CHUNK_SIZE = 20;

    private final static QName qname = new QName("http://index.web.search.chuprikov.ru/", "WebIndexDBImplService");

    private WebIndexDB webIndexDB;

    @ManagedProperty(value = "#{control}")
    private Control control;

    @ManagedProperty(value = "#{documents}")
    private Documents documents;

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    @PostConstruct
    private void init() {
        try {
            URL url = new URL("http://localhost:8080/WebServices/IndexDB?wsdl");
            webIndexDB = Service.create(url, qname).getPort(WebIndexDB.class);
            reset();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PostingInfo[] getLastInfos() {
        return lastInfos;
    }

    public long getTermID() {
        return termID;
    }

    public void setTermID(long termID) {
        this.termID = termID;
    }

    private long termID;

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public void reset() throws Exception {
        PostingInfo first = webIndexDB.readFirstPosting(termID);
        if (first == null)
            lastInfos = new PostingInfo[0];
        else {
            lastInfos = new PostingInfo[1];
            lastInfos[0] = first;
        }
        advance();
    }

    public String show(long termID) throws Exception {
        this.termID = termID;
        control.setActiveIdx(4);
        reset();
        return "postingsList";
    }

    public void advance() throws Exception {
        if (lastInfos != null && lastInfos.length > 0) {
            lastInfos = webIndexDB.readNextPostings(termID, lastInfos[lastInfos.length - 1].getDocumentID(), CHUNK_SIZE);
        }
    }

    public String findDocument(long documentID) throws Exception {
        documents.setRequestDocumentID(documentID);
        documents.resetWith();
        control.setActiveIdx(6);
        return "control";
    }
}
