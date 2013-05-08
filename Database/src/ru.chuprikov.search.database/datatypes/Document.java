package ru.chuprikov.search.database.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Document {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProblemID getProblemID() {
        return problemID;
    }

    public void setProblemID(ProblemID problemID) {
        this.problemID = problemID;
    }

    public long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(long documentID) {
        this.documentID = documentID;
    }

    public Document() {

    }

    public Document(long documentID, ProblemID problemID, String url) {
        this.documentID = documentID;
        this.problemID = problemID;
        this.url = url;
    }

    private long documentID;
    private String url;
    private ProblemID problemID;
}
