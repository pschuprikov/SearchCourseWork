package ru.chuprikov.search.database.berkeley;

import com.sleepycat.collections.StoredIterator;
import ru.chuprikov.search.database.CloseableListIterator;

public class BerkeleyCloseableStoredIterator<E> implements CloseableListIterator<E> {
    private final StoredIterator<E> berkeleyIterator;

    BerkeleyCloseableStoredIterator(StoredIterator<E> berkeleyIterator) {
        this.berkeleyIterator = berkeleyIterator;
    }

    @Override
    public void close() throws Exception {
        berkeleyIterator.close();
    }

    @Override
    public boolean hasNext() {
        return berkeleyIterator.hasNext();
    }

    @Override
    public E next() {
        return berkeleyIterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return berkeleyIterator.hasPrevious();
    }

    @Override
    public E previous() {
        return berkeleyIterator.previous();
    }

    @Override
    public int nextIndex() {
        return berkeleyIterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return berkeleyIterator.previousIndex();
    }

    @Override
    public void remove() {
        berkeleyIterator.remove();
    }

    @Override
    public void set(E e) {
        berkeleyIterator.set(e);
    }

    @Override
    public void add(E e) {
        berkeleyIterator.add(e);
    }
}
