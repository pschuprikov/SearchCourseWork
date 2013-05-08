package ru.chuprikov.search.web.terms;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TermInfo {
    private String term;
    private Long id;

    public TermInfo() {
    }

    public TermInfo(Long id, String term) {
        this.id = id;
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
