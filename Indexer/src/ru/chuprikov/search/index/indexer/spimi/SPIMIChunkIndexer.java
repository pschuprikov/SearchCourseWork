package ru.chuprikov.search.index.indexer.spimi;

import ru.chuprikov.search.datatypes.Datatypes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class SPIMIChunkIndexer {
    private final HashMap<String, PostingsListWriter> dictionary = new HashMap<>();
    private final long maxSizeBytes;
    private long postingsSizeBytes = 0;

    SPIMIChunkIndexer(long maxSizeBytes) {
        this.maxSizeBytes = maxSizeBytes;
    }

    boolean isFull() {
        return postingsSizeBytes >= maxSizeBytes;
    }

    public void addToIndex(String term, Datatypes.Posting posting) {
        if (!dictionary.containsKey(term)) {
            dictionary.put(term, new PostingsListWriter());
            postingsSizeBytes += term.length() * 2 + 50;
        }
        postingsSizeBytes += dictionary.get(term).addPosting(posting);
    }

    void writeToTemporary(DataOutputStream outStream) throws IOException {
        ArrayList<String> terms = new ArrayList<>(dictionary.keySet());
        Collections.sort(terms);
        outStream.writeInt(terms.size());
        for (String term : terms) {
            outStream.writeUTF(term);
            dictionary.get(term).writeToStream(outStream);
        }
    }
}
