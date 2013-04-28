package ru.chuprikov.search.gather;

import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.gather.fetcher.Fetcher;
import ru.chuprikov.search.gather.fetcher.NoProxyProvider;
import ru.chuprikov.search.gather.fetcher.ProxyFetcher;
import ru.chuprikov.search.gather.problemsets.ProblemSetLoader;
import ru.chuprikov.search.gather.problemsets.ProblemSets;
import ru.chuprikov.search.gather.problemsets.SPOJProblemIDSLoader;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gather {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Wrong number of arguments");
            System.exit(1);
        }


        Fetcher proxyFetcher = new ProxyFetcher(new NoProxyProvider());
        try (SearchDatabase db = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
             FetchedDB fetchedDB = db.openFetchDB()
        ) {
            ProblemSetLoader loader = new ProblemSetLoader(proxyFetcher, fetchedDB);
            loader.loadURLs(ProblemSets.getRange(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2])));
            proxyFetcher.awaitCompletion();
            System.err.println("Fetch results. Total: " + loader.getNumTotal() + "; Successful: "
                    + loader.getNumSuccessfull() + "; Already fetched: " + loader.getNumAlreadyFetched());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
