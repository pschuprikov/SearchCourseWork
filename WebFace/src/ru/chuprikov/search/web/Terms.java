package ru.chuprikov.search.web;

import ru.chuprikov.search.web.terms.TermInfo;
import ru.chuprikov.search.web.terms.WebTermDB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Terms implements Serializable {
    TermInfo[] lastInfos;
    private static final int CHUNK_SIZE = 20;

    private final static QName qname = new QName("http://terms.web.search.chuprikov.ru/", "WebTermDBImplService");

    private WebTermDB wsdb;

    public String getRequest() {
        return request;
    }

    private String request = "";

    public Terms() {
        try {
            URL url = new URL("http://localhost:8081/WS/terms?wsdl");
            wsdb = Service.create(url, qname).getPort(WebTermDB.class);
            reset();
            advance();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public TermInfo[] getLastInfos() {
        return lastInfos;
    }

    public void resetWith() {
        lastInfos = wsdb.getNextTermInfos(request, CHUNK_SIZE);
    }

    public void reset() {
        lastInfos = new TermInfo[1];
        lastInfos[0] = wsdb.getFirstTermInfo();
        advance();
    }

    public void advance() {
        if (lastInfos != null)
            lastInfos = wsdb.getNextTermInfos(lastInfos[lastInfos.length - 1].getTerm(), CHUNK_SIZE);
    }
}
