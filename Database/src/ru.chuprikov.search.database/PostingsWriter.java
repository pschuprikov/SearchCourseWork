package ru.chuprikov.search.database;

import ru.chuprikov.search.datatypes.Datatypes;

public interface PostingsWriter extends AutoCloseable {
    void appendPosting(Datatypes.Posting posting) throws Exception;
}
