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

        String s = problem.getContent().substring(problem.getContent().indexOf("<H2 class=\"problem_title\">"),
                problem.getContent().indexOf("<TABLE CLASS=\"sample\">"));
        p.title = getTextBody(s, false);
        String term = "<DIV ID=\"problem_text\">";
        String term2 = "<H3 CLASS=\"problem_subtitle\">Исходные данные</H3>";
        String s2 = s.substring(s.indexOf(term), s.indexOf(term2));
        p.condition = getTextBody(s2, true);
        term = "<H3 CLASS=\"problem_subtitle\">Результат</H3>";
        s2 = s.substring(s.indexOf(term2), s.indexOf(term));
        p.inputSpecification = getTextBody(s2, true);
        s2 = s.substring(s.indexOf(term));
        p.outputSpecification = getTextBody(s2, true);

        return p;
    }
}
