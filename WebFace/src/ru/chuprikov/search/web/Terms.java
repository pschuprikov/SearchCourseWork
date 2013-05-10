package ru.chuprikov.search.web;

import ru.chuprikov.search.database.datatypes.Term;
import ru.chuprikov.search.web.terms.WebTermDB;

import javax.annotation.PostConstruct;
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
    private Term[] lastInfos;
    private static final int CHUNK_SIZE = 20;

    private final static QName qname = new QName("http://terms.web.search.chuprikov.ru/", "WebTermDBImplService");

    private WebTermDB webTermDB;

    public String getRequest() {
        return request;
    }

    private String request = "";

    @PostConstruct
    private void init() {
        try {
            URL url = new URL("http://localhost:8081/WS/terms?wsdl");
            webTermDB = Service.create(url, qname).getPort(WebTermDB.class);
            reset();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Term[] getLastInfos() {
        return lastInfos;
    }

    public void resetWith() {
        lastInfos = webTermDB.getNextTerms(request, CHUNK_SIZE);
    }

    public void reset() throws Exception {
        Term first = webTermDB.getFirstTerm();
        if (first == null)
            lastInfos = new Term[0];
        else {
            lastInfos = new Term[1];
            lastInfos[0] = first;
        }
        advance();
    }

    public void advance() {
        if (lastInfos != null && lastInfos.length > 0)
            lastInfos = webTermDB.getNextTerms(lastInfos[lastInfos.length - 1].getTerm(), CHUNK_SIZE);
    }
}
