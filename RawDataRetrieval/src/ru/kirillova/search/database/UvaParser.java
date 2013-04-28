package ru.kirillova.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

/**
 * Created with IntelliJ IDEA.
 * User: Asus
 * Date: 28.04.13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class UvaParser implements  ParserContent {

        private Problem p;

        public UvaParser(ProblemRawData content) {
            p = new Problem();
            parse(content.getUrl(), content.getProblemID(), content.getResource(), content.getContent());
        }

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

        public void parse (String url, String id, String resource, String body) {
            p.url = url;
            p.resource = resource;
            p.problemID = id;
            String s = body.substring(body.indexOf(";<A NAME=\"SECTION0001000000000000000000\">"));
            s = s.replaceAll("&nbsp;", " ");
            String term = "<A NAME=\"SECTION0001000000000000000000\">";
            String s2 = s.substring(s.indexOf(term));
            p.title = getTextBody(s2, false, 0);
            term = "</A>";
            String term2 = "<A NAME=\"SECTION0001001000000000000000\">";
            s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.condition = getTextBody(s2, true, 0);
            term = "<A NAME=\"SECTION0001002000000000000000\">";
            if (s.indexOf(term) == -1) return;
            s2 = s.substring(s.indexOf(term2), s.indexOf(term));
            p.input_specification = getTextBody(s2, true, 1);
            term2 = "<A NAME=\"SECTION0001003000000000000000\">";
            s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.output_specification = getTextBody(s2, true, 1);
        }

        public Problem getProblem() {
            return p;
        }
}
