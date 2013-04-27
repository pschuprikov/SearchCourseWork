package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
class TimusProblemRange implements ProblemRange {

    private final int first;
    private final int last;
    private int current;

    private static ProblemLoader loader = new StandardProblemLoader();

    TimusProblemRange(int first, int last) {
        this.first = first;
        this.last = last;
        current = first;
    }

    @Override
    public boolean hasNext() {
        return current < last;
    }

    @Override
    public ProblemFetchData next() {
        current++;
        return new ProblemFetchData(
            "timus", new Integer(current).toString(),
            new String("http://acm.timus.ru/problem.aspx?num=" + current), this
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
    public ProblemLoader getLoader() {
        return loader;
    }
}
