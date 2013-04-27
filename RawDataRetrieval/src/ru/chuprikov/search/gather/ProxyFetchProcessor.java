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
public class ProxyFetchProcessor implements Runnable {
    ProxyProvider proxies;

    ProblemFetchData problem;
    FetchCompletionHandler handler;

    private static int NUM_OF_RETRIES = 5;
    private static int CONNECT_TIMEOUT_MILLS = 100;

    public ProxyFetchProcessor(ProxyProvider proxies, ProblemFetchData problem, FetchCompletionHandler handler) {
        this.proxies = proxies;
        this.problem = problem;
        this.handler = handler;
    }

    @Override
    public void run() {
        System.err.println("Start fetching url: " + problem.url);

        problem.setContent(null);
        Exception lastException = null;
        try {
            URL url = new URL(problem.url);

            for (int i = 0; i < NUM_OF_RETRIES; i++) {
                try {
                    URLConnection conn = url.openConnection(proxies.getProxy());
                    conn.setConnectTimeout(CONNECT_TIMEOUT_MILLS);

                    BufferedReader contentReader =  new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder contentBuilder = new StringBuilder();
                    while (true) {
                        String line = contentReader.readLine();
                        if (line == null)
                            break;
                        contentBuilder.append(line).append("\n");
                    }

                    problem.setContent(contentBuilder.toString());
                } catch (IOException ex) {
                    lastException = ex;
                    continue;
                }
                break;
            }
        } catch (MalformedURLException e) {
            handler.fetchFailed(problem, e);
        }

        if (problem.getContent() == null) {
            handler.fetchFailed(problem, lastException);
        } else {
            handler.handleFetchCompletion(problem);
        }
    }
}
