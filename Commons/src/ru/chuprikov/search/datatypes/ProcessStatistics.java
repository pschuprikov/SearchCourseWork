package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStatistics {
    private int numTotal;
    private int numSuccessful;
    private int numAlreadyFetched;

    public ProcessStatistics() {

    }

    public ProcessStatistics(int numTotal, int numSuccessful, int numAlreadyFetched) {
        this.numTotal = numTotal;
        this.numSuccessful = numSuccessful;
        this.numAlreadyFetched = numAlreadyFetched;
    }

    public String getMessage() {
        return "total: " + this.getNumTotal() + "; succ: " + this.getNumSuccessful() + " already: " + this.getNumAlreadyFetched();
    }

    public int getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }

    public int getNumSuccessful() {
        return numSuccessful;
    }

    public void setNumSuccessful(int numSuccessful) {
        this.numSuccessful = numSuccessful;
    }

    public int getNumAlreadyFetched() {
        return numAlreadyFetched;
    }

    public void setNumAlreadyFetched(int numAlreadyFetched) {
        this.numAlreadyFetched = numAlreadyFetched;
    }
}
