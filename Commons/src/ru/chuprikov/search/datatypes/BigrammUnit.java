package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BigrammUnit {
    private long termID;
    private int termLength;

    public BigrammUnit() {

    }

    public BigrammUnit(long termID, int termLength) {
        this.termID = termID;
        this.termLength = termLength;
    }

    public long getTermID() {
        return termID;
    }

    public void setTermID(long termID) {
        this.termID = termID;
    }

    public int getTermLength() {
        return termLength;
    }

    public void setTermLength(int termLength) {
        this.termLength = termLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigrammUnit that = (BigrammUnit) o;

        if (termID != that.termID) return false;
        if (termLength != that.termLength) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (termID ^ (termID >>> 32));
        result = 31 * result + termLength;
        return result;
    }
}
