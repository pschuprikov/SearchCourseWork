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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term1 = (Term) o;

        if (count != term1.count) return false;
        if (termID != term1.termID) return false;
        if (term != null ? !term.equals(term1.term) : term1.term != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (termID ^ (termID >>> 32));
    }
}
