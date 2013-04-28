package ru.chuprikov.search.database;

public interface SearchDatabase extends AutoCloseable {
    FetchedDB openFetchDB() throws Exception;
    ParsedDB openParseDB() throws Exception;
}
