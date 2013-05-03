package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.Datatypes;

public interface DocumentDB extends AutoCloseable {
    long addDocument(Datatypes.Document document) throws Exception;
    Datatypes.Document getDocument(long documentID) throws Exception;
}
