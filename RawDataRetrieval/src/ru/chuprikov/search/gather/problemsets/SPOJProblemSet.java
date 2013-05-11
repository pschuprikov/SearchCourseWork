package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.datatypes.ProblemID;
import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SPOJProblemSet implements ProblemSet {
    private final int last;
    private int current;

    private final List<String> ids = new ArrayList<>();

    private static final URLContentLoader<ProblemFetchInfo> loader = new StandardURLContentLoader<>();

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
        return new ProblemFetchInfo(new ProblemID(ProblemSets.ProblemSetName.SPOJ.toString(), ids.get(Math.min(current, ids.size() - 1))),
            "http://www.spoj.com/problems/" + ids.get(Math.min(current, ids.size() - 1)), this
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
