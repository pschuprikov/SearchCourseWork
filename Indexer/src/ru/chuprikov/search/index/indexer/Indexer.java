package ru.chuprikov.search.index.indexer;

import ru.chuprikov.search.database.datatypes.Datatypes;

import java.io.IOException;

public interface Indexer extends AutoCloseable {
    void addToIndex(String term, Datatypes.Posting posting) throws IOException;
}
