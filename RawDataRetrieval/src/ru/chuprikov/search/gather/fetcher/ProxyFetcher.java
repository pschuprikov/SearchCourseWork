package ru.chuprikov.search.gather.fetcher;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyFetcher implements Fetcher {
    private final ProxyProvider proxies;
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
    private final Random rng = new Random();

    public ProxyFetcher(ProxyProvider proxies) {
        this.proxies = proxies;
    }

    @Override
    public <T> void fetchProblemAsync(FetchInfo<T> data, FetchCompletionHandler<T> handler) {
        System.err.println("Post fetch of " + data.toString());
        pool.schedule(new ProxyFetchProcessor(proxies, data, handler), rng.nextInt(500) + 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void awaitCompletion() throws InterruptedException {
        pool.shutdown();
        while (!pool.awaitTermination(10, TimeUnit.SECONDS));
    }
}
