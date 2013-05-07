package ru.chuprikov.search.database;

import java.util.ListIterator;

public interface CloseableListIterator<T> extends CloseableIterator<T>, ListIterator<T> {
}
