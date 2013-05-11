package ru.kirillova.search.database;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemRawData;

class EolimpContentParser implements ContentParser {
        private String getTextBody(String s, boolean flag, int skip) {
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
            if (!problem.getContent().contains("href='problems-print")) return p;
            String s = problem.getContent().substring(problem.getContent().indexOf("href='problems-print"));
            String term = "<h1>";
            s = s.replaceAll("&nbsp;", " ");
            s = s.replaceAll("&ndash;", "-");
            if (!s.contains(term)) return p;
            p.setTitle(getTextBody(s.substring(s.indexOf(term)), false, 0));
            term = "<div class='condition'>";
            String term2 = "<h2>Технические условия</h2>";
            if (!s.contains(term2)) {
                term2 = "<h2>Specifications</h2>";
                if (!s.contains(term2)) return p;
            }
            String s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.setCondition(getTextBody(s2, true, 0));
            term =  "<strong>Входные данные</strong>";
            if (!s.contains(term)) {
                term = "<strong>Input</strong>";
                if (!s.contains(term)) return p;
            }
            term2 = "<strong>Выходные данные</strong>";
            if (!s.contains(term2)) {
                term2 = "<strong>Output</strong>";
                if (!s.contains(term2)) return p;
            }
            s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.setInputSpecification(getTextBody(s2, true, 1));
            term = "<h2>Информация о задаче</h2>";
            if (!s.contains(term)) {
                term = "<h2>Problem information</h2>";
                if (!s.contains(term)) return p;
            }
            s2 = s.substring(s.indexOf(term2), s.indexOf(term));
            p.setOutputSpecification(getTextBody(s2, true, 1));
            return p;
        }
}
