package ru.chuprikov.search.database;

public interface SearchDatabase extends AutoCloseable {
    FetchedDB openFetchedDB() throws Exception;
    void truncateFetchedDB() throws Exception;

    ParsedDB openParsedDB() throws Exception;
    void truncateParsedDB() throws Exception;

    IndexDB openIndexDB(int maxPostingsChunkSizeBytes) throws Exception;
    void truncateIndexDB() throws Exception;

    TermDB openTermDB() throws Exception;
    void truncateTermDB() throws Exception;

    DocumentDB openDocumentDB() throws Exception;
    void truncateDocumentDB() throws Exception;
}
