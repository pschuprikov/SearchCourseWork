package ru.chuprikov.search.gather.problemsets;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 5:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemSets {
    public static ProblemSet getRange(String id, int first, int last) {
        switch (id) {
            case "uva": return new UVaProblemSet(first, last);
            case "cf" : return new CodeforcesProblemSet(first, last);
            case "eolimp" : return new EolimpProblemSet(first, last);
            case "timus" : return new TimusProblemSet(first, last);
            default: return null;
        }
    }
}
