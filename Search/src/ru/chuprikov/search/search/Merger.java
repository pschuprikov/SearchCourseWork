package ru.chuprikov.search.search;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class Merger implements Iterator<PostingInfo> {
    private final List<Iterator<PostingInfo>> options;
    private final PriorityQueue<IndexedPostingInfo> queue = new PriorityQueue<>();

    private static class IndexedPostingInfo implements Comparable<IndexedPostingInfo> {
        private final PostingInfo postingInfo;
        private final int optionIdx;

        private IndexedPostingInfo(int optionIdx, PostingInfo postingInfo) {
            this.optionIdx = optionIdx;
            this.postingInfo = postingInfo;
        }

        @Override
        public int compareTo(IndexedPostingInfo o) {
            return postingInfo.compareTo(o.postingInfo);
        }
    }

    public Merger(List<Iterator<PostingInfo>> options) {
        this.options = options;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).hasNext())
                queue.add(new IndexedPostingInfo(i, options.get(i).next()));
        }
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public PostingInfo next() {
        IndexedPostingInfo cur = queue.poll();
        if (options.get(cur.optionIdx).hasNext())
            queue.add(new IndexedPostingInfo(cur.optionIdx, options.get(cur.optionIdx).next()));
        return cur.postingInfo;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
