package ru.chuprikov.search.database;

import ru.kirillova.search.database.ParsedProblem;

public interface ParsedDB extends AutoCloseable {
    void saveParsed(ParsedProblem parsedProblem) throws Exception;
    boolean contains(ParsedProblem parsedProblem) throws Exception;
    CloseableIterator<ParsedProblem> openIterator();
}
