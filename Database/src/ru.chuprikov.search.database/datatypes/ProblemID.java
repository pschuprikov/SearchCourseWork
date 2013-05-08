package ru.chuprikov.search.database.datatypes;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class ProblemID implements Comparable<ProblemID>, Serializable {
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

    @Override
    public int compareTo(ProblemID o) {
        if (!getResource().equals(o.getResource())) {
            return getResource().compareTo(o.getResource());
        } else {
            return getProblemID().compareTo(o.getProblemID());
        }
    }
}
