package ru.chuprikov.search.database.datatypes;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class ParsedProblem implements Serializable {

    public ProblemID getProblemID() {
        return problemID;
    }

    public void setProblemID(ProblemID problemID) {
        this.problemID = problemID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInputSpecification() {
        return inputSpecification;
    }

    public void setInputSpecification(String inputSpecification) {
        this.inputSpecification = inputSpecification;
    }

    public String getOutputSpecification() {
        return outputSpecification;
    }

    public void setOutputSpecification(String outputSpecification) {
        this.outputSpecification = outputSpecification;
    }

    public String getBlock(Datatypes.Posting.PositionType positionType) {
        switch (positionType) {
            case CONDITION: return getCondition();
            case INPUT_SPEC: return getInputSpecification();
            case OUTPUT_SPEC: return getOutputSpecification();
            case TITLE: return getTitle();
            default: throw new AssertionError("Unknown position type");
        }
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private ProblemID problemID;
    private String url = "";
    private String title = "";
    private String inputSpecification = "";
    private String outputSpecification = "";
    private String condition = "";

    public ParsedProblem() {

    }
    public ParsedProblem(ProblemRawData raw) {
        this.problemID = raw.getProblemID();
        this.url = raw.getUrl();
    }

    public ParsedProblem(ProblemID problemID, String url) {
        this.problemID = problemID;
        this.url = url;
    }
}
