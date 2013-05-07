package ru.chuprikov.search.web.fetch;

import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.gather.fetcher.Fetcher;
import ru.chuprikov.search.gather.fetcher.NoProxyProvider;
import ru.chuprikov.search.gather.fetcher.ProxyFetcher;
import ru.chuprikov.search.gather.problemsets.ProblemSetLoader;
import ru.chuprikov.search.gather.problemsets.ProblemSets;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;

@WebService(endpointInterface = "ru.chuprikov.search.web.fetch.WebFetch")
public class WebFetchImpl implements WebFetch {
    private SearchDatabase searchDB;
    private FetchedDB fetchedDB;

    @PostConstruct
    private void openDatabaseConnections() {
        try {
            searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            fetchedDB = searchDB.openFetchDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void closeDatabaseConnections() {
        try {
            fetchedDB.close();
            searchDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FetchStatistics fetch(String resource, int from, int to) throws Exception {
        Fetcher proxyFetcher = new ProxyFetcher(new NoProxyProvider());
        ProblemSetLoader loader = new ProblemSetLoader(proxyFetcher, fetchedDB);

        loader.loadURLs(ProblemSets.getRange(ProblemSets.ProblemSetName.valueOf(resource), from, to));
        proxyFetcher.awaitCompletion();

        FetchStatistics response = new FetchStatistics();
        response.setNumTotal(loader.getNumTotal());
        response.setNumSuccessful(loader.getNumSuccessfull());
        response.setNumAlreadyFetched(loader.getNumAlreadyFetched());
        return response;
    }
}
