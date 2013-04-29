package ru.kirillova.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

class SpojContentParser implements ContentParser {
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
                    } else if (!Character.isLetter(s.charAt(i + 1))) {
                        if (skip == 0) {
                            str.append(s.charAt(i));
                        }
                        continue;
                    }
                    for (; (i < s.length()) && (s.charAt(i) != '>'); ++i)
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
            if (!problem.getContent().contains(("<meta property=\"og:type\" content=\"spoj-pl:problem\"/> "))) return p;
            String s = problem.getContent().substring(
                    problem.getContent().indexOf("<meta property=\"og:type\" content=\"spoj-pl:problem\"/> "));
            s = s.toLowerCase();
            s = s.replaceAll("&nbsp;", "");
            String term = "<h1>";
            String s2 = s.substring(s.indexOf(term));
            p.title = getTextBody(s2, false, 0);
            term = "<p align=\"justify\">";
            String term2 = "<h3>input</h3>";
            if (!s.contains(term2)) return p;
            if (!s.contains(term)) return p;
            s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.condition = getTextBody(s2, true, 0);
            term = "<h3>output</h3>";
            if (!s.contains(term)) return p;
            s2 = s.substring(s.indexOf(term2), s.indexOf(term));
            p.inputSpecification = getTextBody(s2, true, 1);
            term2 = "<h3>example</h3>";
            if (s.indexOf(term2, s.indexOf(term) + 1) == -1) return p;
            s2 = s.substring(s.indexOf(term), s.indexOf(term2, s.indexOf(term) + 1));
            p.outputSpecification = getTextBody(s2, true, 1);
            return p;
        }
}
