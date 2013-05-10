package ru.chuprikov.search.search;

import java.util.Iterator;

public class Merger implements Iterator<PostingInfo> {
    private final Iterator<PostingInfo> fst;
    private final Iterator<PostingInfo> snd;
    private final Joiner join;

    private PostingInfo next = null;


    public Merger(Iterator<PostingInfo> fst, Iterator<PostingInfo> snd, Joiner join) {
        this.fst = fst;
        this.snd = snd;
        this.join = join;
        advance();
    }

    public boolean hasNext() {
        return next != null;
    }

    @Override
    public PostingInfo next() {
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    void advance() {
        PostingInfo firstInfo = null;
        PostingInfo secondInfo = null;
        while(true) {
            if (!fst.hasNext() && firstInfo == null || !snd.hasNext() && secondInfo == null) {
                firstInfo = null;
                secondInfo = null;
                break;
            }
            if (firstInfo == null) {
                firstInfo = fst.next();
            }
            if (secondInfo == null) {
                secondInfo = snd.next();
            }
            if (secondInfo.compareTo(firstInfo) == 0)
                break;
            else if (secondInfo.compareTo(firstInfo) < 0)
                secondInfo = null;
            else
                firstInfo = null;
        }
        if (firstInfo == null || secondInfo == null)
            next = null;
        else {
            next = new PostingInfo(firstInfo.getDocumentID(), firstInfo.getPositionType());
            next.positions().addAll(join.join(firstInfo.positions(), secondInfo.positions()));
        }
    }
}
