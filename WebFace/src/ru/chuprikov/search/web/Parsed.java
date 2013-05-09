package ru.chuprikov.search.web;

import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Parsed {

    @ManagedProperty(value = "#{parser}")
    private Parser parser;

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @ManagedProperty(value="#{param.resource}")
    private String resource;

    @ManagedProperty(value="#{param.problemID}")
    private String problemID;


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

    public ParsedProblem getProblem() {
        return problem;
    }

    private ParsedProblem problem;

    @PostConstruct
    public void init() {
        problem = parser.getWebParse().getProblemParsed(new ProblemID(resource, problemID));
    }

}
