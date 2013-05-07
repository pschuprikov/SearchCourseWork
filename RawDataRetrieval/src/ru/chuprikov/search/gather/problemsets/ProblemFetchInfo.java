package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;
import ru.chuprikov.search.gather.fetcher.FetchInfo;
import ru.chuprikov.search.gather.fetcher.URLContentLoader;

public class ProblemFetchInfo extends ProblemRawData implements FetchInfo<ProblemFetchInfo> {
    public final ProblemSet problemSet;

    public ProblemFetchInfo(ProblemID problemID, String url, ProblemSet problemSet) {
        super(problemID, url, null);

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
