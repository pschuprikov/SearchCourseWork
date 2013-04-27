package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */

class ProblemFetchData extends ProblemRawData {
    final ProblemRange range;

    public ProblemFetchData(String resource, String problemID, String url, ProblemRange range) {
        super(resource, problemID, url, null);
        this.range = range;
    }
}
