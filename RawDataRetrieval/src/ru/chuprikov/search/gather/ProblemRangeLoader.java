package ru.chuprikov.search.gather;

import ru.chuprikov.search.database.FetchedDB;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
class ProblemRangeLoader implements FetchCompletionHandler {
    private final Fetcher fetcher;
    private final FetchedDB fetchedDB;

    private int numTotal;
    private int numAlreadyFetched;
    private AtomicInteger numSuccessfull = new AtomicInteger();

    int getNumTotal() {
        return numTotal;
    }

    int getNumSuccessfull() {
        return numSuccessfull.get();
    }

    int getNumAlreadyFetched() {
        return numAlreadyFetched;
    }

    ProblemRangeLoader(Fetcher fetcher, FetchedDB fetchedDB) {
        this.fetcher = fetcher;
        this.fetchedDB = fetchedDB;
    }

    void loadURLs(ProblemRange range) throws Exception {
        while (range.hasNext()) {
            numTotal++;

            final ProblemFetchData currentProblem = range.next();
            if (!fetchedDB.contains(currentProblem))
                fetcher.fetchProblemAsync(currentProblem, this);
            else {
                numAlreadyFetched++;
                System.err.println("Already fetched: " + currentProblem);
            }
        }
    }

    @Override
    public void handleFetchCompletion(ProblemFetchData problem)  {
        if (problem.range.checkValid(problem.content)) {
            numSuccessfull.incrementAndGet();
            System.err.println("Successful fetch: " + problem);
            try {
                fetchedDB.saveFetched(problem);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Validate failed: " + problem);
        }
    }

    @Override
    public void fetchFailed(ProblemFetchData problem, Exception reason) {
        System.err.println("Fetch of " + problem + " failed");
        System.err.println(reason.getMessage());
    }
}
