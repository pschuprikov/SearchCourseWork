package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 5:10 AM
 * To change this template use File | Settings | File Templates.
 */
class ProblemRanges {
    static ProblemRange getRange(String id, int first, int last) {
        switch (id) {
            case "uva": return new UVaProblemRange(first, last);
            case "cf" : return new CodeforcesProblemRange(first, last);
            case "eolimp" : return new EolimpProblemRange(first, last);
            case "timus" : return new EolimpProblemRange(first, last);
            default: return null;
        }
    }
}
