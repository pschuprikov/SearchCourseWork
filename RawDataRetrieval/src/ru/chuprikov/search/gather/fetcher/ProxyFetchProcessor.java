package ru.chuprikov.search.gather.fetcher;

import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
class ProxyFetchProcessor <T> implements Runnable {
    private final ProxyProvider proxies;

    private final FetchInfo<T> data;
    private final FetchCompletionHandler<T> handler;

    private final Random rng = new Random();

    private static final int NUM_OF_RETRIES = 5;

    ProxyFetchProcessor(ProxyProvider proxies, FetchInfo<T> data, FetchCompletionHandler<T> handler) {
        this.proxies = proxies;
        this.data = data;
        this.handler = handler;
    }

    @Override
    public void run() {
        System.err.println("Start fetching url: " + data.toString());

        String content = null;
        Exception lastException = null;
        for (int i = 0; i < NUM_OF_RETRIES; i++) {
            try {
                Thread.sleep(1500 + rng.nextInt(400));
                content = data.getLoader().loadContent(data, proxies.getProxy());
            } catch (IOException ex) {
                lastException = ex;
                continue;
            } catch (InterruptedException e) {
                handler.fetchFailed(data.get(), e);
                Thread.currentThread().interrupt();
                return;
            }
            break;
        }

        if (content == null) {
            handler.fetchFailed(data.get(), lastException);
        } else {
            handler.handleFetchCompletion(data.get(), content);
        }
    }
}
