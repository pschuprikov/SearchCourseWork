package ru.chuprikov.search.database.datatypes;

public class ParsedProblem {

    public ProblemID problemID;
    public String url;
    public String title;
    public String inputSpecification;
    public String outputSpecification;
    public String condition;

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
