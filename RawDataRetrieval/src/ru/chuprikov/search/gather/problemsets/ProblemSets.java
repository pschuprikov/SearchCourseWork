package ru.chuprikov.search.gather.problemsets;

import java.io.IOException;

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
