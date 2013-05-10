package ru.chuprikov.search.web;

import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.web.index.WebIndexer;

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
public class Index implements Serializable {
    private final static QName qname = new QName("http://index.web.search.chuprikov.ru/", "WebIndexerImplService");
    private static URL url;

    static {
        try {
            url = new URL("http://localhost:8081/WS/indexer?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private final WebIndexer webIndexer;

    public Index() {
        webIndexer = Service.create(url, qname).getPort(WebIndexer.class);
    }

    private ProblemID problemIDFrom = new ProblemID();
    private ProblemID problemIDTo = new ProblemID();

    public ProblemID getProblemIDTo() {
        return problemIDTo;
    }

    public void setProblemIDTo(ProblemID problemIDTo) {
        this.problemIDTo = problemIDTo;
    }

    public ProblemID getProblemIDFrom() {
        return problemIDFrom;
    }

    public void setProblemIDFrom(ProblemID problemIDFrom) {
        this.problemIDFrom = problemIDFrom;
    }

    public int getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public void setMaxMemoryUsage(int maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

    public int getMaxPostingsChunkSize() {
        return maxPostingsChunkSize;
    }

    public void setMaxPostingsChunkSize(int maxPostingsChunkSize) {
        this.maxPostingsChunkSize = maxPostingsChunkSize;
    }

    private int maxMemoryUsage = 10000000;
    private int maxPostingsChunkSize = 1000000;

    public void index() throws Exception {
        FacesContext.getCurrentInstance().addMessage("index",
            new FacesMessage("Index result", webIndexer.index(problemIDFrom, problemIDTo, maxMemoryUsage, maxPostingsChunkSize).getMessage()));
    }

    public void indexAll() throws Exception {
        FacesContext.getCurrentInstance().addMessage("index",
                new FacesMessage("Index result", webIndexer.indexAll(maxMemoryUsage, maxPostingsChunkSize).getMessage()));
    }
}
