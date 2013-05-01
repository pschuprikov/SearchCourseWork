package ru.chuprikov.search.index.indexer.spimi;

import ru.chuprikov.search.database.datatypes.Datatypes;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class PostingsListWriter {
    private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private final DataOutputStream out = new DataOutputStream(byteStream);
    private int countPostings = 0;

    int addPosting(Datatypes.Posting posting) {
        int oldSize = out.size();
        try {
            posting.writeDelimitedTo(out);
            countPostings++;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return out.size() - oldSize;
    }

    void writeToStream(DataOutputStream outStream) throws IOException {
        outStream.writeInt(countPostings);
        outStream.write(byteStream.toByteArray(), 0, byteStream.size());
    }
}
