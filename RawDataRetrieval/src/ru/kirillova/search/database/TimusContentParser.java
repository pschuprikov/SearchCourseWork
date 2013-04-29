package ru.kirillova.search.database;
import ru.chuprikov.search.gather.ProblemRawData;

class TimusContentParser implements ContentParser {
    private String getTextBody(String s, boolean flag) {
        int skip = 0;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '<') {
                if (s.charAt(i + 1) == '/') {
                    if (skip > 0) {
                        --skip;
                    }
                    if (!flag) {
                        return str.toString();
                    }
                }
                if (s.indexOf("<H3 CLASS=\"problem_subtitle\">", i) == i) {
                    ++skip;
                }
                for (; s.charAt(i) != '>'; ++i)
                    ;
                continue;
            }
            if (skip == 0) {
                str.append(s.charAt(i));
            }
        }
        return str.toString();
    }

    public ParsedProblem parseContent(ProblemRawData problem) {
        ParsedProblem p = new ParsedProblem(problem);
        String term = "<TABLE CLASS=\"sample\">";
        if (!problem.getContent().contains(term)) {
            term = "<DIV CLASS=\"problem_source\">";
            if (!problem.getContent().contains(term)) return p;
        }
        String s = problem.getContent().substring(problem.getContent().indexOf("<H2 class=\"problem_title\">"),
                    problem.getContent().indexOf(term));
        p.title = getTextBody(s, false);
        term = "<DIV ID=\"problem_text\">";
        String term2 = "<H3 CLASS=\"problem_subtitle\">Исходные данные</H3>";
        if (!s.contains(term2)) {
            term2 = "<H3 CLASS=\"problem_subtitle\">Input</H3>";
            if (!s.contains(term2)) return p;
        }
        String s2 = s.substring(s.indexOf(term), s.indexOf(term2));
        p.condition = getTextBody(s2, true);
        term = "<H3 CLASS=\"problem_subtitle\">Результат</H3>";
        if (!s.contains(term)) {
            term = "<H3 CLASS=\"problem_subtitle\">Output</H3>";
            if (!s.contains(term)) return p;
        }
        s2 = s.substring(s.indexOf(term2), s.indexOf(term));
        p.inputSpecification = getTextBody(s2, true);
        s2 = s.substring(s.indexOf(term));
        p.outputSpecification = getTextBody(s2, true);
        return p;
    }
}
