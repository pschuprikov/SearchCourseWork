package ru.chuprikov.search.web;

import ru.chuprikov.search.web.fetch.FetchStatistics;
import ru.chuprikov.search.web.fetch.WebFetch;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Fetch implements Serializable {
    private final static QName qname = new QName("http://fetch.web.search.chuprikov.ru/", "WebFetchImplService");
    private static URL url;

    static {
        try {
             url = new URL("http://localhost:8081/WS/fetch?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private final WebFetch webFetchService;

    public Fetch() {
        webFetchService = Service.create(url, qname).getPort(WebFetch.class);
    }

    private String resource = "";
    private int from;
    private int to;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public FetchStatistics getStat() {
        return stat;
    }

    private FetchStatistics stat = new FetchStatistics();

    public void fetch() {
        try {
            stat = webFetchService.fetch(resource, from, to);
            FacesContext.getCurrentInstance().addMessage("response", new FacesMessage(FacesMessage.SEVERITY_INFO, "Fetch result",
                "total: " + stat.getNumTotal() + "; succ: " + stat.getNumSuccessful() + " already: " + stat.getNumAlreadyFetched()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
