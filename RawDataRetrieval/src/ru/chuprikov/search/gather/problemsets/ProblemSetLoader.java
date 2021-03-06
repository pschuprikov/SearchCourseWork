package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.gather.fetcher.FetchCompletionHandler;
import ru.chuprikov.search.gather.fetcher.Fetcher;

import java.util.concurrent.atomic.AtomicInteger;

public class ProblemSetLoader implements FetchCompletionHandler<ProblemFetchInfo> {
    private final Fetcher fetcher;
    private final FetchedDB fetchedDB;

    private int numTotal;
    private int numAlreadyFetched;
    private final AtomicInteger numSuccessfull = new AtomicInteger();

    public int getNumTotal() {
        return numTotal;
    }

    public int getNumSuccessfull() {
        return numSuccessfull.get();
    }

    public int getNumAlreadyFetched() {
        return numAlreadyFetched;
    }

    public ProblemSetLoader(Fetcher fetcher, FetchedDB fetchedDB) {
        this.fetcher = fetcher;
        this.fetchedDB = fetchedDB;
    }

    public void loadURLs(ProblemSet range) throws Exception {
        while (range.hasNext()) {
            numTotal++;

            final ProblemFetchInfo currentProblem = range.next();
            if (!fetchedDB.contains(currentProblem.getProblemID()))
                fetcher.fetchProblemAsync(currentProblem, this);
            else {
                numAlreadyFetched++;
                System.err.println("Already fetched: " + currentProblem);
            }
        }
    }

    @Override
    public void handleFetchCompletion(ProblemFetchInfo problem, String content)  {
        if (problem.problemSet.checkValid(content)) {
            problem.setContent(content);
            numSuccessfull.incrementAndGet();

            System.err.println("Successful fetch: " + problem);
            try {
                fetchedDB.saveFetched(problem);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Validate failed: " + problem);
        }
    }

    @Override
    public void fetchFailed(ProblemFetchInfo problem, Exception reason) {
        System.err.println("Fetch of " + problem + " failed");
        System.err.println(reason.getMessage());
    }
}
