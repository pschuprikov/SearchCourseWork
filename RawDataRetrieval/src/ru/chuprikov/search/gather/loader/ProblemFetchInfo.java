package ru.chuprikov.search.gather.loader;

import ru.chuprikov.search.gather.ProblemRawData;
import ru.chuprikov.search.gather.fetcher.FetchInfo;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;
import ru.chuprikov.search.gather.problemsets.ProblemSet;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProblemFetchInfo extends ProblemRawData implements FetchInfo<ProblemFetchInfo> {
    public final ProblemSet problemSet;

    public ProblemFetchInfo(String resource, String problemID, String url, ProblemSet problemSet) {
        super(resource, problemID, url, null);
        this.problemSet = problemSet;
    }

    @Override
    public URLContentLoader<ProblemFetchInfo> getLoader() {
        return problemSet.getLoader();
    }

    @Override
    public ProblemFetchInfo get() {
        return this;
    }
}
