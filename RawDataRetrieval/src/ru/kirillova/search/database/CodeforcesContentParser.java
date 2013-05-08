package ru.kirillova.search.database;

import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

class CodeforcesContentParser implements ContentParser {

    private String getTextBody(String s, int index, int skipfirst) {
        StringBuilder str = new StringBuilder();
        int count = 0;
        for (int i = index; i < s.length(); ++i) {
            if (s.charAt(i) == '<') {
                if (s.charAt(i + 1) == '/') {
                    if (skipfirst > 0) {
                        --skipfirst;
                    }
                    --count;
                } else {
                    ++count;
                }
                for (; s.charAt(i) != '>'; ++i);
                if (count == 0) {
                    return str.toString();
                }
                continue;
            }
            if (skipfirst == 0) {
                str.append(s.charAt(i));
            }
        }
        return str.toString();
    }

    public ParsedProblem parseContent(ProblemRawData problem) {
        ParsedProblem p = new ParsedProblem(problem);
        if (!problem.getContent().contains("<div class=\"problem-statement\">")) return p;
        if (!problem.getContent().contains("<div class=\"sample-test\">")) return p;
        String s = problem.getContent().substring(problem.getContent().indexOf("<div class=\"problem-statement\">"),
            problem.getContent().indexOf("<div class=\"sample-test\">"));
        String term = "<div class=\"title\">";
        if (!s.contains(term)) return p;
        p.setTitle(getTextBody(s, s.indexOf(term), 0));
        term = "<div>";
        if (!s.contains(term)) return p;
        p.setCondition(getTextBody(s, s.indexOf(term), 0));
        term = "<div class=\"input-specification\">";
        if (!s.contains(term)) return p;
        p.setInputSpecification(getTextBody(s, s.indexOf(term), 1));
        term = "<div class=\"output-specification\">";
        if (!s.contains(term)) return p;
        p.setOutputSpecification(getTextBody(s, s.indexOf(term), 1));
        return p;
    }
}
