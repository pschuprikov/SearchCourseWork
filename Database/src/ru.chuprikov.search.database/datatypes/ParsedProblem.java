package ru.chuprikov.search.database.datatypes;

public class ParsedProblem {

    public String resource;
    public String problemID;
    public String url;
    public String title;
    public String inputSpecification;
    public String outputSpecification;
    public String condition;

    public ParsedProblem() {

    }
    public ParsedProblem(ProblemRawData raw) {
        this.resource = raw.getResource();
        this.problemID = raw.getProblemID();
        this.url = raw.getUrl();
    }

    public ParsedProblem(String resource, String problemID, String url) {
        this.resource = resource;
        this.problemID = problemID;
        this.url = url;
    }
}
