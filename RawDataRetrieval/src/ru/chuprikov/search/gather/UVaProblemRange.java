package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 1:50 AM
 * To change this template use File | Settings | File Templates.
 */
class UVaProblemRange implements ProblemRange {
    final int first;
    final int last;
    int current;

    private static ProblemLoader problemLoader = new UVaProblemLoader();

    UVaProblemRange(int first, int last) {
        this.first = first;
        this.last = last;
        current = first - 1;
    }

    @Override
    public boolean checkValid(String content) {
        return content.contains("Input") && content.contains("Output");
    }

    @Override
    public ProblemLoader getLoader() {
         return problemLoader;
    }

    @Override
    public boolean hasNext() {
        return current < last;
    }

    @Override
    public ProblemFetchData next() {
        current++;
        return new ProblemFetchData("UVa", new Integer(current).toString(),
                new String("http://uva.onlinejudge.org/external/" + new Integer(current/100).toString() + "/" +
                new Integer(current).toString() + ".html"), this);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
