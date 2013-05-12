package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.Document;
import ru.chuprikov.search.datatypes.SearchResponse;

import javax.annotation.PostConstruct;
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
public class Search implements Serializable{
    private String request;

    private final static QName qname = new QName("http://search.web.search.chuprikov.ru/", "WebSearchImplService");
    private static URL url;

    private WebSearch webSearch;

    public Document[] getSearchResult() {
        return searchResult;
    }

    private Document[] searchResult;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private int limit = 20;

    static {
        try {
            url = new URL("http://localhost:8080/WebServices/Search?wsdl");
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
        searchResult = new Document[0];
        SearchResponse response = webSearch.searchSimpleConjunction(request, limit);

        FacesContext.getCurrentInstance().addMessage("search",
            new FacesMessage("Search result", "Time taken: " + response.getTimeMills() + "ms"));


        for (int i = 0; i < response.getSuggestions().length; i++) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(i == 0 ? "Suggesstion (searched):" : "Suggestion: ", response.getSuggestions()[i]));
        }
        searchResult = response.getFoundDocuments();
    }

    public String showContent() throws Exception {
        return "parsedProblem";
    }
}
