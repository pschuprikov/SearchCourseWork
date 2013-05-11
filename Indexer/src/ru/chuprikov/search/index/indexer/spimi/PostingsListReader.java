package ru.chuprikov.search.index.indexer.spimi;

import ru.chuprikov.search.datatypes.Datatypes;

import java.io.DataInputStream;
import java.io.IOException;

class PostingsListReader implements Comparable<PostingsListReader> {
    private final DataInputStream dis;
    private int length = 0;
    private int current = 0;

    private Datatypes.Posting currentPosting;

    PostingsListReader(DataInputStream dis) {
        this.dis = dis;
    }

    boolean hasNext() {
        return current < length;
    }

    Datatypes.Posting getCurrentPosting() {
        return currentPosting;
    }

    void readNextPostingList() throws IOException {
        length = dis.readInt();
        current = 0;
    }

    void advance() throws IOException {
        if (hasNext()) {
            currentPosting = Datatypes.Posting.parseDelimitedFrom(dis);
            current++;
        }
    }

    @Override
    public int compareTo(PostingsListReader o) {
        return Long.compare(getCurrentPosting().getDocumentID(), o.getCurrentPosting().getDocumentID());
    }
}
