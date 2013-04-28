package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
class EolimpProblemRange implements ProblemRange {
    final int first;
    final int last;
    int current;

    private static ProblemLoader problemLoader = new StandardProblemLoader();

    EolimpProblemRange(int first, int last) {
        this.first = first;
        this.last = last;
        current = first - 1;
    }


    @Override
    public boolean checkValid(String content) {
        return content.contains("<h2>Problem information</h2>");
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
        return new ProblemFetchData("eolimp", new Integer(current).toString(),
                new String("http://www.e-olimp.com/en/problems/" + current), this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
