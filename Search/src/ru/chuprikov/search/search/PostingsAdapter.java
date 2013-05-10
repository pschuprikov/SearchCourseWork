package ru.chuprikov.search.search;

import ru.chuprikov.search.database.datatypes.Datatypes;

import java.util.ArrayDeque;
import java.util.Iterator;

public class PostingsAdapter implements Iterator<PostingInfo> {
    private final Iterator<Datatypes.Posting> postingIterator;
    private final ArrayDeque<PostingInfo> currentPostings = new ArrayDeque<>();

    public PostingsAdapter(Iterator<Datatypes.Posting> postingIterator) {
        this.postingIterator = postingIterator;
    }

    @Override
    public boolean hasNext() {
        return !currentPostings.isEmpty() || postingIterator.hasNext();
    }

    @Override
    public PostingInfo next() {
        if (currentPostings.isEmpty()) {
            Datatypes.Posting nextPosting = postingIterator.next();
            PostingInfo postingInfo = null;
            for (Datatypes.Posting.Position pos : nextPosting.getPositionsList()) {
                if (postingInfo == null) {
                    postingInfo = new PostingInfo(nextPosting.getDocumentID(), pos.getType());
                }
                if (postingInfo.getPositionType() == pos.getType()) {
                    postingInfo.positions().add(new PositionInfo(pos.getIndex(), pos.getIndex()));
                } else {
                    currentPostings.addLast(postingInfo);
                    postingInfo = null;
                }
            }
            if (postingInfo != null)
            currentPostings.addLast(postingInfo);
        }
        return currentPostings.pollFirst();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
