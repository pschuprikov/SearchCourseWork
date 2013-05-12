package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.misc.ProblemSetName;

import java.io.IOException;

public class ProblemSets {
    public static ProblemSet getRange(ProblemSetName id, int first, int last) throws IOException {
        switch (id) {
            case UVA: return new UVaProblemSet(first, last);
            case CF : return new CodeforcesProblemSet(first, last);
            case EOLIMP : return new EolimpProblemSet(first, last);
            case TIMUS : return new TimusProblemSet(first, last);
            case SPOJ : return new SPOJProblemSet(first, last);
            default: return null;
        }
    }
}
