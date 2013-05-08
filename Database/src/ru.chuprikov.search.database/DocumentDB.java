package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.Document;

public interface DocumentDB extends AutoCloseable {
    long addDocument(Document document) throws Exception;

    Document get(long documentID) throws Exception;
    CloseableIterator<Document> iterator() throws Exception;
    CloseableIterator<Document> upperBound(long documentID) throws Exception;
}
