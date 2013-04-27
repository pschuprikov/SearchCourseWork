package ru.chuprikov.search.gather;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
class CodeforcesProblemRange implements ProblemRange {
    final int firstRound;
    final int lastRound;

    int currentRound;
    char currentLetter;

    private static ProblemLoader loader = new StandardProblemLoader();

    CodeforcesProblemRange(int firstRound, int lastRound) {
        this.firstRound = firstRound;
        this.lastRound = lastRound;

        currentRound = firstRound;
        currentLetter = 'A' - 1;
    }

    @Override
    public boolean hasNext() {
        return currentLetter < 'E' || currentRound < lastRound;
    }

    @Override
    public ProblemFetchData next() {
        if (currentLetter < 'E') {
            currentLetter++;
        } else if (currentRound < lastRound) {
            currentRound++;
            currentLetter = 'A';
        } else {
            return null;
        }

        return new ProblemFetchData(
            "codeforces", new String("" + currentRound + currentLetter),
            new String("http://www.codeforces.ru/problemset/problem/" + currentRound + "/" + currentLetter), this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkValid(String content) {
        return content.contains("<div class=\"problem-statement\">");
    }

    @Override
    public ProblemLoader getLoader() {
        return loader;
    }
}
