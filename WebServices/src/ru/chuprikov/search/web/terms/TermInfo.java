package ru.chuprikov.search.web.terms;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TermInfo {
    private String term;
    private Long id;

    public String getTerm() {
        return term;
    }

    @XmlAttribute
    public void setTerm(String term) {
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }
}
