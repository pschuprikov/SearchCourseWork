package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResponse {
    private long timeMills = 0;
    private String[] suggestions = new String[0];
    private Document[] foundDocuments = new Document[0];

    public SearchResponse() {

    }

    public SearchResponse(long timeMills, String[] suggestions, Document[] foundDocuments) {
        this.timeMills = timeMills;
        this.suggestions = suggestions;
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

    public String[] getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String[] suggestions) {
        this.suggestions = suggestions;
    }

    public void setTimeMills(long timeMills) {
        this.timeMills = timeMills;
    }
}
