package ru.chuprikov.search.database;

import ru.chuprikov.search.datatypes.ParsedProblem;
import ru.chuprikov.search.datatypes.ProblemID;

import javax.jws.WebService;

@WebService
public interface ParsedDB extends AutoCloseable {
    void saveParsed(ParsedProblem parsedProblem) throws Exception;

    boolean contains(ProblemID problemID) throws Exception;

    ParsedProblem get(ProblemID problemID);
    CloseableListIterator<ParsedProblem> iterator();
    CloseableListIterator<ParsedProblem> upperBound(ProblemID problemID);
}
