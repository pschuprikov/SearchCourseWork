package ru.kirillova.search.database;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemRawData;
import ru.chuprikov.search.misc.ProblemSetName;

import java.util.HashMap;
import java.util.Map;

public class MultiSiteContentParser implements ContentParser {
    private static final Map<String, ContentParser> parsers = new HashMap<>();

    public MultiSiteContentParser() {
        parsers.put(ProblemSetName.UVA.toString(), new UvaContentParser());
        parsers.put(ProblemSetName.SPOJ.toString(), new SpojContentParser());
        parsers.put(ProblemSetName.EOLIMP.toString(), new EolimpContentParser());
        parsers.put(ProblemSetName.TIMUS.toString(), new TimusContentParser());
        parsers.put(ProblemSetName.CF.toString(), new CodeforcesContentParser());
    }

    @Override
    public ParsedProblem parseContent(ProblemRawData problem) {
        return parsers.get(problem.getProblemID().getResource()).parseContent(problem);
    }
}
