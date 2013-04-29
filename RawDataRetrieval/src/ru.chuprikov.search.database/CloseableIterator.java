package ru.chuprikov.search.database;

import java.util.Iterator;

public interface CloseableIterator<T> extends Iterator<T>, AutoCloseable {

}
