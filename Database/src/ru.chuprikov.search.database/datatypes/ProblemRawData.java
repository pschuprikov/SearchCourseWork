package ru.chuprikov.search.database.datatypes;

public class ProblemRawData {
    public final String url;
    public final String resource;

    public String getProblemID() {
        return problemID;
    }

    public final String problemID;
    public String content;


    public ProblemRawData(String resource, String problemID, String url, String content) {
        this.resource = resource;
        this.problemID = problemID;
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "ProblemRawData{" +
                "problemID='" + problemID + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }

    public String getResource() {
        return resource;
    }
}
