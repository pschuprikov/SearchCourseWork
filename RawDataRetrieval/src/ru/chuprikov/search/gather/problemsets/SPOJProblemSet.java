package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SPOJProblemSet implements ProblemSet {
    private final int last;
    private int current;

    private final List<String> ids = new ArrayList<>();

    private static final URLContentLoader<ProblemFetchInfo> loader = new StandardURLContentLoader<ProblemFetchInfo>();

    SPOJProblemSet(int first, int last) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("spojids"))) {
            while (true) {
                final String line = br.readLine();
                if (line == null)
                    break;
                if (!line.isEmpty())
                    ids.add(line);
            }
        }
        this.last = last;
        current = first - 1;
    }

    @Override
    public boolean checkValid(String content) {
        return content.contains("Problem code");
    }

    @Override
    public URLContentLoader<ProblemFetchInfo> getLoader() {
        return loader;
    }

    @Override
    public boolean hasNext() {
        return current < last;
    }

    @Override
    public ProblemFetchInfo next() {
        current++;
        return new ProblemFetchInfo("spoj", ids.get(Math.min(current, ids.size() - 1)),
            "http://www.spoj.com/problems/" + ids.get(Math.min(current, ids.size() - 1)), this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}