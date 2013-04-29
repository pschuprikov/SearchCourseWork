package ru.kirillova.search.database;

import ru.chuprikov.search.gather.ProblemRawData;

import java.util.HashMap;
import java.util.Map;

class MultiSiteContentParser implements ContentParser {
    private static final Map<String, ContentParser> parsers = new HashMap<>();

    MultiSiteContentParser() {
        parsers.put("UVa", new UvaContentParser());
        parsers.put("spoj", new SpojContentParser());
        parsers.put("eolimp", new EolimpContentParser());
        parsers.put("timus", new TimusContentParser());
        parsers.put("codeforces", new CodeforcesContentParser());
    }

    @Override
    public ParsedProblem parseContent(ProblemRawData problem) {
        return parsers.get(problem.getResource()).parseContent(problem);
    }
}
