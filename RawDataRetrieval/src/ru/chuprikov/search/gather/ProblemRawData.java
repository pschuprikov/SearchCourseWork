package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemRawData {
    public final String url;
    public final String resource;
    public final String problemID;
    public String content;


    public ProblemRawData(String resource, String problemID, String url, String content) {
        this.url = url;
        this.resource = resource;
        this.problemID = problemID;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "ProblemFetchData{" +
                "problemID='" + problemID + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}
