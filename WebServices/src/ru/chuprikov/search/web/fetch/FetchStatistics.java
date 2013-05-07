package ru.chuprikov.search.web.fetch;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FetchStatistics {
    private int numTotal;
    private int numSuccessful;
    private int numAlreadyFetched;

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
