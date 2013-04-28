package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;

class EolimpProblemSet implements ProblemSet {
    private final int last;
    private int current;

    private static final URLContentLoader<ProblemFetchInfo> URLContentLoader = new StandardURLContentLoader<>();

    EolimpProblemSet(int first, int last) {
        this.last = last;
        current = first - 1;
    }


    @Override
    public boolean checkValid(String content) {
        return content.contains("<h2>Problem information</h2>");
    }

    @Override
    public URLContentLoader<ProblemFetchInfo> getLoader() {
        return URLContentLoader;
    }

    @Override
    public boolean hasNext() {
        return current < last;
    }

    @Override
    public ProblemFetchInfo next() {
        current++;
        return new ProblemFetchInfo("eolimp", Integer.toString(current),
                "http://www.e-olimp.com/en/problems/" + current, this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
