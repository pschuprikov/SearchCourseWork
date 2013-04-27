package ru.chuprikov.search.gather;

import ru.chuprikov.search.database.FetchedDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gather implements Runnable {

    public static void main(String[] args) {
         new Thread(new Gather()).start();
    }

    @Override
    public void run() {
        Fetcher proxyFetcher = new ProxyFetcher(new NoProxyProvider());
        try (SearchDatabase db = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
             FetchedDB fetchedDB = db.openFetchDB()
        ) {
            ProblemRangeLoader loader = new ProblemRangeLoader(proxyFetcher, fetchedDB);
            loader.loadURLs(new UVaProblemRange(100, 100));
            proxyFetcher.awaitCompletion();
            System.err.println("Fetch results. Total: " + loader.getNumTotal() + "; Successful: "
                + loader.getNumSuccessfull() + "; Already fetched: " + loader.getNumAlreadyFetched());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
