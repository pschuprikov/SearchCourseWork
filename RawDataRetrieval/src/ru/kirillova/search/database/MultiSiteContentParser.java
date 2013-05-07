package ru.kirillova.search.database;

import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemRawData;
import ru.chuprikov.search.gather.problemsets.ProblemSets;

import java.util.HashMap;
import java.util.Map;

class MultiSiteContentParser implements ContentParser {
    private static final Map<String, ContentParser> parsers = new HashMap<>();

    MultiSiteContentParser() {
        parsers.put(ProblemSets.ProblemSetName.UVA.toString(), new UvaContentParser());
        parsers.put(ProblemSets.ProblemSetName.SPOJ.toString(), new SpojContentParser());
        parsers.put(ProblemSets.ProblemSetName.EOLIMP.toString(), new EolimpContentParser());
        parsers.put(ProblemSets.ProblemSetName.TIMUS.toString(), new TimusContentParser());
        parsers.put(ProblemSets.ProblemSetName.CF.toString(), new CodeforcesContentParser());
    }

    @Override
    public ParsedProblem parseContent(ProblemRawData problem) {
        return parsers.get(problem.getProblemID().getResource()).parseContent(problem);
    }
}
