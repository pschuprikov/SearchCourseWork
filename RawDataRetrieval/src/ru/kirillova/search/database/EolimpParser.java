package ru.kirillova.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

/**
 * Created with IntelliJ IDEA.
 * User: Asus
 * Date: 28.04.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class EolimpParser implements ParserContent {
        private Problem p;

        public EolimpParser (ProblemRawData content) {
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

        public void parse(String url, String id, String resource, String body) {
            p.url = url;
            p.resource = resource;
            p.problemID = id;
            String s = body.substring(body.indexOf("href='problems-print"),
                    body.indexOf("<h2>Информация о задаче</h2>"));
            String term = "<h1>";
            s = s.replaceAll("&nbsp;", " ");
            s = s.replaceAll("&ndash;", "-");
            p.title = getTextBody(s.substring(s.indexOf(term)), false, 0);
            term = "<div class='condition'>";
            String term2 = "<h2>Технические условия</h2>";
            String s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.condition = getTextBody(s2, true, 0);
            term =  "<strong>Входные данные</strong>";
            term2 = "<strong>Выходные данные</strong>";
            s2 = s.substring(s.indexOf(term), s.indexOf(term2));
            p.input_specification = getTextBody(s2, true, 1);
            s2 = s.substring(s.indexOf(term2));
            p.output_specification = getTextBody(s2, true, 1);
        }

        public Problem getProblem() {
            return p;
        }
}
