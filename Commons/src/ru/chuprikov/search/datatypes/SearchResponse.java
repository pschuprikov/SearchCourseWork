package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResponse {
    private long timeMills = 0;
    private String correctedRequest = "";
    private Document[] foundDocuments = new Document[0];

    public SearchResponse() {

    }

    public SearchResponse(long timeMills, String correctedRequest, Document[] foundDocuments) {
        this.timeMills = timeMills;
        this.correctedRequest = correctedRequest;
        this.foundDocuments = foundDocuments;
    }

    public Document[] getFoundDocuments() {
        return foundDocuments;
    }

    public void setFoundDocuments(Document[] foundDocuments) {
        this.foundDocuments = foundDocuments;
    }

    public long getTimeMills() {
        return timeMills;
    }

    public void setTimeMills(long timeMills) {
        this.timeMills = timeMills;
    }

    public String getCorrectedRequest() {
        return correctedRequest;
    }

    public void setCorrectedRequest(String correctedRequest) {
        this.correctedRequest = correctedRequest;
    }
}
