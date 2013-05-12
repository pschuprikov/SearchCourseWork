package ru.chuprikov.search.web;


import ru.chuprikov.search.datatypes.ProblemID;
import ru.chuprikov.search.datatypes.ProblemRawData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Fetches implements Serializable {
    private ProblemRawData[] lastDatas;
    private static final int CHUNK_SIZE = 20;

    private final static QName qname = new QName("http://fetch.web.search.chuprikov.ru/", "WebFetchImplService");

    transient private WebFetch webFetch;

    private String requestResource = "OLOLO";
    private String requestProblemID = "";

    public String getRequestResource() {
        return requestResource;
    }

    public void setRequestResource(String requestResource) {
        this.requestResource = requestResource;
    }

    public String getRequestProblemID() {
        return requestProblemID;
    }

    public void setRequestProblemID(String requestProblemID) {
        this.requestProblemID = requestProblemID;
    }

    public Fetches() throws Exception {
        try {
            URL url = new URL("http://localhost:8080/WebServices/Fetch?wsdl");
            webFetch = Service.create(url, qname).getPort(WebFetch.class);
            reset();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ProblemRawData[] getLastDatas() {
        return lastDatas;
    }

    public void resetWith() throws Exception {
         lastDatas = webFetch.getNextProblemRawDatas(new ProblemID(requestResource, requestProblemID), CHUNK_SIZE);
    }

    public void reset() throws Exception {
        ProblemRawData first = webFetch.getFirstProblemRawData();
        if (first == null)
            lastDatas = new ProblemRawData[0];
        else {
            lastDatas = new ProblemRawData[1];
            lastDatas[0] = first;
        }
        advance();
    }

    public void advance() throws Exception {
        if (lastDatas != null && lastDatas.length > 0)
            lastDatas = webFetch.getNextProblemRawDatas(lastDatas[lastDatas.length - 1].getProblemID(), CHUNK_SIZE);
    }

    public void clear() throws Exception {
        webFetch.clearFetches();
    }
}
