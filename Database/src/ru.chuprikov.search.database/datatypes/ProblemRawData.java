package ru.chuprikov.search.database.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProblemRawData {
    private String url = "";
    private String content = "";
    private ProblemID problemID = new ProblemID();

    public ProblemRawData() {
    }

    public ProblemRawData(ProblemID problemID, String url, String content) {
        this.problemID = problemID;
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProblemID getProblemID() {
        return problemID;
    }

    public void setProblemID(ProblemID problemID) {
        this.problemID = problemID;
    }

    @Override
    public String toString() {
        return "ProblemRawData{" +
                "problemID='" + problemID + '\'' +
                '}';
    }

}
