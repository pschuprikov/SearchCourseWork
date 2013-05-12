package ru.chuprikov.search.search;

import ru.chuprikov.search.search.joiners.Joiner;

import java.util.Iterator;

public class Intersector implements Iterator<PostingInfo> {
    private final Iterator<PostingInfo> fst;
    private final Iterator<PostingInfo> snd;
    private final Joiner join;

    private PostingInfo next = null;


    public Intersector(Iterator<PostingInfo> fst, Iterator<PostingInfo> snd, Joiner join) {
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
        PostingInfo result =  next;
        advance();
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void advance() {
        while (true) {

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

            if (firstInfo == null || secondInfo == null) {
                next = null;
                break;
            } else {
                next = new PostingInfo(firstInfo.getDocumentID(), firstInfo.getPositionType());
                next.positions().addAll(join.join(firstInfo.positions(), secondInfo.positions()));
                if (next.positions().isEmpty())
                    continue;
                else
                    break;
            }
        }
    }
}
