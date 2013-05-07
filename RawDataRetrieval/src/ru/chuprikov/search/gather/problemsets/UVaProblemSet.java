package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;

class UVaProblemSet implements ProblemSet {
    private final int last;
    private int current;

    private static final URLContentLoader<ProblemFetchInfo> URLContentLoader = new UVaURLContentLoader();

    UVaProblemSet(int first, int last) {
        this.last = last;
        current = first - 1;
    }

    @Override
    public boolean checkValid(String content) {
        // TODO: remove this shit.
        return (content.contains("Input") || content.contains("input") || content.contains("INPUT")) &&
                (content.contains("Output") || content.contains("output") || content.contains("OUTPUT"));
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
        return new ProblemFetchInfo(new ProblemID(ProblemSets.ProblemSetName.UVA.toString(), Integer.toString(current)),
                "http://uva.onlinejudge.org/external/" + Integer.toString(current / 100) + "/" +
                current + ".html", this);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
