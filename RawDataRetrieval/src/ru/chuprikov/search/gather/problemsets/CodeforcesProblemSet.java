package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.URLContentLoader;
import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
class CodeforcesProblemSet implements ProblemSet {
    private final int lastRound;

    private int currentRound;
    private char currentLetter;

    private static final URLContentLoader<ProblemFetchInfo> loader = new StandardURLContentLoader<>();

    CodeforcesProblemSet(int firstRound, int lastRound) {
        this.lastRound = lastRound;

        currentRound = firstRound - 1;
        currentLetter = 'E';
    }

    @Override
    public boolean hasNext() {
        return currentLetter < 'E' || currentRound < lastRound;
    }

    @Override
    public ProblemFetchInfo next() {
        if (currentLetter < 'E') {
            currentLetter++;
        } else if (currentRound < lastRound) {
            currentRound++;
            currentLetter = 'A';
        } else {
            return null;
        }

        return new ProblemFetchInfo(
            "codeforces", "" + currentRound + currentLetter,
            "http://www.codeforces.ru/problemset/problem/" + currentRound + "/" + currentLetter, this
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
    public URLContentLoader<ProblemFetchInfo> getLoader() {
        return loader;
    }
}
