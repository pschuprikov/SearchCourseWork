package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.ParsedProblem;

import javax.jws.WebService;

@WebService
public interface ParsedDB extends AutoCloseable {
    void saveParsed(ParsedProblem parsedProblem) throws Exception;
    boolean contains(ParsedProblem parsedProblem) throws Exception;
    CloseableIterator<ParsedProblem> openIterator();
}
