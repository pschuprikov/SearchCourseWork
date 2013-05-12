package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.ProcessStatistics;

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
             url = new URL("http://localhost:8080/WebServices/Fetch?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private WebFetch webFetchService;

    public Fetch() {
        webFetchService = Service.create(url, qname).getPort(WebFetch.class);
    }

    private String resource = "OLOLO";
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

    public ProcessStatistics getStat() {
        return stat;
    }

    private ProcessStatistics stat = new ProcessStatistics();

    public void fetch() {
        try {
            stat = webFetchService.fetch(resource, from, to);
            FacesContext.getCurrentInstance().addMessage("fetch_response", new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Fetch result", stat.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
