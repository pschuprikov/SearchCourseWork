package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Term {
    private long termID;
    private long count;
    private String term;

    public Term() {

    }

    public Term(String term, long termID, long count) {
        this.termID = termID;
        this.count = count;
        this.term = term;
    }

    public long getTermID() {
        return termID;
    }

    public void setTermID(long termID) {
        this.termID = termID;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
