package ru.chuprikov.search.database.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProblemID {
    @Override
    public String toString() {
        return "ProblemID{" +
                "resource='" + resource + '\'' +
                ", problemID='" + problemID + '\'' +
                '}';
    }

    private String resource = "";
    private String problemID = "";

    public ProblemID() {
    }

    public ProblemID(String resource, String problemID) {
        this.resource = resource;
        this.problemID = problemID;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getProblemID() {
        return problemID;
    }

    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }
}
