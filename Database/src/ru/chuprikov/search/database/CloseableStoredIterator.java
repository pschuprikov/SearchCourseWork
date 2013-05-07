package ru.chuprikov.search.database;

import java.util.Iterator;

public interface CloseableStoredIterator<T> extends Iterator<T>, AutoCloseable {
}
