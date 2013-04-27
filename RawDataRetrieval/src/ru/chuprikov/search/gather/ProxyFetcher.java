package ru.chuprikov.search.gather;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyFetcher implements Fetcher {
    final ProxyProvider proxies;
    final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
    final Random rng = new Random();

    ProxyFetcher(ProxyProvider proxies) {
        this.proxies = proxies;
    }

    @Override
    public void fetchProblemAsync(ProblemFetchData problem, FetchCompletionHandler handler) {
        System.err.println("Post fetch of " + problem.toString());
        pool.schedule(new ProxyFetchProcessor(proxies, problem, handler), rng.nextInt(500) + 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void awaitCompletion() throws InterruptedException {
        pool.shutdown();
        while (!pool.awaitTermination(10, TimeUnit.SECONDS));
    }
}
