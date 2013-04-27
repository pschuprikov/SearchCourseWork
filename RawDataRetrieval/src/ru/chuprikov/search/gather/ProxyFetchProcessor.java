package ru.chuprikov.search.gather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
class ProxyFetchProcessor implements Runnable {
    private final ProxyProvider proxies;

    private final ProblemFetchData problem;
    private final FetchCompletionHandler handler;

    private static int NUM_OF_RETRIES = 5;

    ProxyFetchProcessor(ProxyProvider proxies, ProblemFetchData problem, FetchCompletionHandler handler) {
        this.proxies = proxies;
        this.problem = problem;
        this.handler = handler;
    }

    @Override
    public void run() {
        System.err.println("Start fetching url: " + problem.url);

        problem.setContent(null);
        Exception lastException = null;
        for (int i = 0; i < NUM_OF_RETRIES; i++) {
            try {
                problem.setContent(problem.range.getLoader().load(problem, proxies.getProxy()));
            } catch (IOException ex) {
                lastException = ex;
                continue;
            }
            break;
        }

        if (problem.getContent() == null) {
            handler.fetchFailed(problem, lastException);
        } else {
            handler.handleFetchCompletion(problem);
        }
    }
}
