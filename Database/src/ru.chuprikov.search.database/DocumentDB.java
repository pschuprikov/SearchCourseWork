package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.Datatypes;

import java.util.Iterator;

public interface DocumentDB extends AutoCloseable {
    long addDocument(Datatypes.Document document) throws Exception;
    Datatypes.Document getDocument(long documentID) throws Exception;

    Iterator<Long> iterator() throws Exception;
    Iterator<Long> upperBound(long first) throws Exception;
}
