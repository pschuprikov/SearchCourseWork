package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.URLContentLoader;
import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
class TimusProblemSet implements ProblemSet {
    private final int last;
    private int current;

    private static final URLContentLoader<ProblemFetchInfo> loader = new StandardURLContentLoader<>();

    TimusProblemSet(int first, int last) {
        this.last = last;
        current = first;
    }

    @Override
    public boolean hasNext() {
        return current < last;
    }

    @Override
    public ProblemFetchInfo next() {
        current++;
        return new ProblemFetchInfo(
            "timus", Integer.toString(current),
            "http://acm.timus.ru/problem.aspx?num=" + current, this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkValid(String content) {
        return content.contains("<DIV CLASS=\"problem_content\">");
    }

    @Override
    public URLContentLoader<ProblemFetchInfo> getLoader() {
        return loader;
    }
}
