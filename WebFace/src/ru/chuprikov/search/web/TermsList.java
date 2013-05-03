package ru.chuprikov.search.web;

import ru.chuprikov.search.database.web.TermInfo;
import ru.chuprikov.search.database.web.WebSearchDB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class TermsList implements Serializable {

    TermInfo[] lastInfos;
    private URL url;
    private static final int CHUNK_SIZE = 30;

    private final static QName qname = new QName("http://web.database.search.chuprikov.ru/", "WebSearchDBImplService");

    private WebSearchDB wsdb;

    public TermsList() {
        try {
            url = new URL("http://localhost:8081/WS/db?wsdl");
            Service service = Service.create(url, qname);
            wsdb = service.getPort(WebSearchDB.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public TermInfo[] getLastInfos() {
        return lastInfos;
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
