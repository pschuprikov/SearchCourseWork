package ru.chuprikov.search.index.indexer.spimi;

import ru.chuprikov.search.database.BigrammDB;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.PostingsWriter;
import ru.chuprikov.search.database.TermDB;
import ru.chuprikov.search.datatypes.BigrammUnit;
import ru.chuprikov.search.datatypes.Datatypes;
import ru.chuprikov.search.index.indexer.Indexer;

import java.io.*;
import java.util.ArrayDeque;
import java.util.PriorityQueue;

public class SPIMIIndexer implements Indexer {
    private final long maxMemoryUsage;
    private final File temporaryFolder;

    private int chunkIdx = 0;
    private SPIMIChunkIndexer chunkIndexer;

    private final IndexDB indexDB;
    private final BigrammDB bigrammDB;

    private boolean closed = false;
    private final TermDB termDB;

    public SPIMIIndexer(File temporaryFolder, IndexDB indexDB, TermDB termDB, BigrammDB bigrammDB, long maxMemoryUsageBytes) {
        this.indexDB = indexDB;
        this.termDB = termDB;
        this.bigrammDB = bigrammDB;
        this.maxMemoryUsage = maxMemoryUsageBytes;
        this.temporaryFolder = temporaryFolder;
        this.temporaryFolder.mkdirs();
        chunkIndexer = new SPIMIChunkIndexer(maxMemoryUsageBytes);
    }

    @Override
    public void addToIndex(String term, Datatypes.Posting posting) throws IOException {
        checkClosed();
        if (chunkIndexer.isFull()) {
            advanceChunkIndexer();
        }
        chunkIndexer.addToIndex(term, posting);
    }

    private void advanceChunkIndexer() throws IOException {
        try(DataOutputStream stream = getOutStream(chunkIdx)) {
            chunkIndexer.writeToTemporary(stream);
        }
        chunkIndexer = new SPIMIChunkIndexer(maxMemoryUsage);
        chunkIdx++;
    }

    private void checkClosed() {
        if (closed)
            throw new UnsupportedOperationException("SPIMI Indexer has been closed.");
    }

    private DataOutputStream getOutStream(int chunkIdx) throws FileNotFoundException {
        return new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(temporaryFolder.toPath().resolve("chunk" + chunkIdx).toFile())));
    }

    private DataInputStream getInStream(int chunkIdx) throws FileNotFoundException {
        return new DataInputStream(
            new BufferedInputStream(new FileInputStream(temporaryFolder.toPath().resolve("chunk" + chunkIdx).toFile())));
    }

    private void processPostings(String term, PriorityQueue<PostingsListReader> postings) throws Exception {
        boolean isTermNew = !termDB.contains(term);
        final long termID = termDB.add(term);
        if (isTermNew) processKgramm(term, termID);

        try (PostingsWriter writer = indexDB.getPostingsWriter(termID)) {
            long count = 0;
            while (!postings.isEmpty()) {
                final PostingsListReader currentReader = postings.poll();
                writer.appendPosting(currentReader.getCurrentPosting());
                count++;
                if (currentReader.hasNext()) {
                    currentReader.advance();
                    postings.add(currentReader);
                }
            }
            termDB.incrementCount(term, count);
        }
    }

    private void processKgramm(String term, long termID) {
        if (term.length() < 2)
            return;
        final BigrammUnit unit = new BigrammUnit(termID, term.length());
        for (int i = 0; i < term.length(); i++) {
            bigrammDB.add(term.substring(i, i + 1) + term.charAt(i < term.length() - 1 ? i + 1 : 0), unit);
        }
        bigrammDB.add("$" + term.substring(0, 2), unit);
        bigrammDB.add(term.substring(term.length() - 2, term.length()) + "$", unit);

        for (int i = 0; i < term.length() - 2; i++) {
            bigrammDB.add(term.substring(i, i + 3), unit);
        }
    }

    @Override
    public void close() throws Exception {
        advanceChunkIndexer();
        checkClosed();
        closed = true;

        PriorityQueue<SPIMIChunkReader> readers = new PriorityQueue<>();
        ArrayDeque<SPIMIChunkReader> currentReaders = new ArrayDeque<>();
        PriorityQueue<PostingsListReader> currentPostings = new PriorityQueue<>();

        for (int i = 0; i < chunkIdx; i++) {
            SPIMIChunkReader reader = new SPIMIChunkReader(getInStream(i));
            if (reader.hasNext()) {
                reader.advance();
                readers.add(reader);
            } else {
                reader.close();
            }
        }

        while (!readers.isEmpty()) {
            String currentTerm = readers.peek().getTerm();
            while (!readers.isEmpty() && readers.peek().getTerm().equals(currentTerm)) {
                SPIMIChunkReader currentReader = readers.poll();
                currentReaders.add(currentReader);
                if (currentReader.getPostingsList().hasNext()) {
                    currentReader.getPostingsList().advance();
                    currentPostings.add(currentReader.getPostingsList());
                } else {
                    currentReader.close();
                }
            }

            processPostings(currentTerm, currentPostings);

            currentPostings.clear();

            while (!currentReaders.isEmpty())
                if (currentReaders.peek().hasNext()) {
                    currentReaders.peek().advance();
                    readers.add(currentReaders.poll());
                } else {
                    currentReaders.poll().close();
                }
        }

        for (int i = 0; i < chunkIdx; i++) {
            temporaryFolder.toPath().resolve("chunk" + chunkIdx).toFile().delete();
        }
    }
}
