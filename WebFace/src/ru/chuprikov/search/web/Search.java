package ru.chuprikov.search.web;

import ru.chuprikov.search.web.search.WebSearch;

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
public class Search implements Serializable{
    private String request;

    private final static QName qname = new QName("http://search.web.search.chuprikov.ru/", "WebSearchImplService");
    private static URL url;

    private WebSearch webSearch;

    public Long[] getSearchResult() {
        return searchResult;
    }

    private Long[] searchResult;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private int limit = 20;

    static {
        try {
            url = new URL("http://localhost:8081/WS/search?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        webSearch = Service.create(url, qname).getPort(WebSearch.class);
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void search() throws Exception {
        searchResult = webSearch.searchSimpleConjunction(request, limit);
    }
}
