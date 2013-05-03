package ru.chuprikov.search.database.web;

import javax.faces.bean.ManagedBean;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@ManagedBean
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
