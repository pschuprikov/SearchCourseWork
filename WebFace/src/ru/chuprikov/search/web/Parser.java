package ru.chuprikov.search.web;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemID;
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
public class Parser implements Serializable {
    private ParsedProblem[] lastDatas;
    private static final int CHUNK_SIZE = 20;

    private final static QName qname = new QName("http://fetch.web.search.chuprikov.ru/", "WebParseImplService");

    private WebParse webParse;

    private String requestResourceFrom = "";
    private String requestProblemIDFrom = "";
    private String requestResourceTo = "";

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

    private String requestProblemIDTo = "";
    private String requestResource = "";
    private String requestProblemID = "";

    WebParse getWebParse() {
        return webParse;
    }

    public String getRequestResourceFrom() {
        return requestResourceFrom;
    }

    public void setRequestResourceFrom(String requestResourceFrom) {
        this.requestResourceFrom = requestResourceFrom;
    }

    public String getRequestProblemIDFrom() {
        return requestProblemIDFrom;
    }

    public void setRequestProblemIDFrom(String requestProblemIDFrom) {
        this.requestProblemIDFrom = requestProblemIDFrom;
    }

    public String getRequestResourceTo() {
        return requestResourceTo;
    }

    public void setRequestResourceTo(String requestResourceTo) {
        this.requestResourceTo = requestResourceTo;
    }

    public String getRequestProblemIDTo() {
        return requestProblemIDTo;
    }

    public void setRequestProblemIDTo(String requestProblemIDTo) {
        this.requestProblemIDTo = requestProblemIDTo;
    }

    public void parseAll() throws Exception {
        ProcessStatistics stat = webParse.parseAll();
        FacesContext.getCurrentInstance().addMessage("parse_response", new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Parse result", stat.getMessage()));
    }

    public void parse() throws Exception {
        ProcessStatistics stat = webParse.parse(new ProblemID(requestResourceFrom, requestProblemIDFrom),
            new ProblemID(requestResourceTo, requestProblemIDTo));
        FacesContext.getCurrentInstance().addMessage("parse_response", new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Parse result", stat.getMessage()));
    }

    public Parser() throws Exception {
        try {
            URL url = new URL("http://localhost:8080/WebServices/Parse?wsdl");
            webParse = Service.create(url, qname).getPort(WebParse.class);
            reset();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ParsedProblem[] getLastDatas() {
        return lastDatas;
    }

    public void resetWith() throws Exception {
        lastDatas = webParse.getNextProblemParseds(new ProblemID(requestResource, requestProblemID), CHUNK_SIZE);
    }

    public void reset() throws Exception {
        ParsedProblem first = webParse.getFirstProblemParsed();
        if (first == null)
            lastDatas = new ParsedProblem[0];
        else {
            lastDatas = new ParsedProblem[1];
            lastDatas[0] = first;
        }
        advance();
    }

    public void advance() throws Exception {
        if (lastDatas != null && lastDatas.length > 0)
            lastDatas = webParse.getNextProblemParseds(lastDatas[lastDatas.length - 1].getProblemID(), CHUNK_SIZE);
    }

    public void clear() throws Exception {
        webParse.clearParsed();
    }

    public String showContent() throws Exception {
        return "parsedProblem";
    }
}
