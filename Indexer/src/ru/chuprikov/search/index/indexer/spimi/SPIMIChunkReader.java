package ru.chuprikov.search.index.indexer.spimi;

import java.io.DataInputStream;
import java.io.IOException;

class SPIMIChunkReader implements AutoCloseable, Comparable<SPIMIChunkReader> {
    private final DataInputStream dis;

    private final PostingsListReader postingsList;
    private final int numTerms;
    private int currentTermIdx;
    private String currentTerm;

    SPIMIChunkReader(DataInputStream dis) throws IOException {
        this.dis = dis;
        numTerms = dis.readInt();
        currentTermIdx = 0;
        postingsList = new PostingsListReader(dis);
    }

    String getTerm() {
        return currentTerm;
    }

    PostingsListReader getPostingsList() {
        return postingsList;
    }

    boolean hasNext() {
        return currentTermIdx < numTerms;
    }

    void advance() throws IOException {
        if (hasNext()) {
            currentTermIdx++;
            currentTerm = dis.readUTF();
            postingsList.readNextPostingList();
            if (currentTerm == null)
                throw new AssertionError();
        }
    }

    @Override
    public void close() throws Exception {
        dis.close();
    }

    @Override
    public int compareTo(SPIMIChunkReader o) {
        return currentTerm.compareTo(o.getTerm());
    }
}
