package ru.chuprikov.search.web.fetch;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;
import ru.chuprikov.search.gather.fetcher.Fetcher;
import ru.chuprikov.search.gather.fetcher.NoProxyProvider;
import ru.chuprikov.search.gather.fetcher.ProxyFetcher;
import ru.chuprikov.search.gather.problemsets.ProblemSetLoader;
import ru.chuprikov.search.gather.problemsets.ProblemSets;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.web.fetch.WebFetch")
public class WebFetchImpl implements WebFetch{
    private SearchDatabase searchDB;
    private FetchedDB fetchedDB;

    @PostConstruct
    private void openDatabaseConnections() {
        try {
            searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            fetchedDB = searchDB.openFetchedDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void closeDatabaseConnections() {
        System.err.println("bye");
        try {
            fetchedDB.close();
            searchDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProcessStatistics fetch(String resource, int from, int to) throws Exception {
        Fetcher proxyFetcher = new ProxyFetcher(new NoProxyProvider());
        ProblemSetLoader loader = new ProblemSetLoader(proxyFetcher, fetchedDB);

        loader.loadURLs(ProblemSets.getRange(ProblemSets.ProblemSetName.valueOf(resource), from, to));
        proxyFetcher.awaitCompletion();

        ProcessStatistics response = new ProcessStatistics();
        response.setNumTotal(loader.getNumTotal());
        response.setNumSuccessful(loader.getNumSuccessfull());
        response.setNumAlreadyFetched(loader.getNumAlreadyFetched());
        return response;
    }

    @Override
    public void clearFetches() throws Exception {
        fetchedDB.close();
        searchDB.truncateFetchedDB();
        fetchedDB = searchDB.openFetchedDB();
    }

    @Override
    public ProblemRawData getProblemRawData(ProblemID problemID) {
        return fetchedDB.get(problemID);
    }

    @Override
    public ProblemRawData getFirstProblemRawData() throws Exception {
        try (CloseableIterator<ProblemRawData> it = fetchedDB.iterator()) {
            ProblemRawData res = it.hasNext() ? it.next() : null;
            return res;
        }
    }

    @Override
    public ProblemRawData[] getNextProblemRawDatas(ProblemID problemID, int count) throws Exception {
        ArrayList<ProblemRawData> result = new ArrayList<>();
        try (CloseableIterator<ProblemRawData> it = fetchedDB.upperBound(problemID)) {
            while (it.hasNext() && result.size() < count) {
                result.add(it.next());
            }
        }
        return result.toArray(new ProblemRawData[result.size()]);
    }
}
