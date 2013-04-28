package ru.chuprikov.search.gather.problemsets;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 5:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemSets {
    public static ProblemSet getRange(String id, int first, int last) throws IOException {
        switch (id) {
            case "uva": return new UVaProblemSet(first, last);
            case "cf" : return new CodeforcesProblemSet(first, last);
            case "eolimp" : return new EolimpProblemSet(first, last);
            case "timus" : return new TimusProblemSet(first, last);
            case "spoj" : return new SPOJProblemSet(first, last);
            default: return null;
        }
    }
}
