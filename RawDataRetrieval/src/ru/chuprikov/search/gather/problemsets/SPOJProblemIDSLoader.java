package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.fetcher.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPOJProblemIDSLoader implements FetchCompletionHandler<SPOJProblemIDSLoader.SPOJProblemList> {

    private final static int FIRST_PAGE = 0;
    private final static int LAST_PAGE = 2450;
    private final static int PAGE_STEP = 50;

    private final Fetcher fetcher;
    private final ConcurrentLinkedQueue<String> ids = new ConcurrentLinkedQueue<>();

    public SPOJProblemIDSLoader(Fetcher fetcher) {
        this.fetcher = fetcher;
    }


    public void loadSPOJ() {
        for (int i = FIRST_PAGE; i <= LAST_PAGE; i += PAGE_STEP) {
            fetcher.fetchProblemAsync(new SPOJProblemList(
                    "http://www.spoj.com/problems/classical/sort=0,start=" + i
            ), this);
        }
    }

    public void writeSPOJIDs() throws FileNotFoundException {
        System.err.println("Number of IDs: " + ids.size());
        try(PrintWriter out = new PrintWriter(new File("spojids"))) {
            for (String s : ids) {
                out.println(s);
            }
        }
    }


    @Override
    public void handleFetchCompletion(SPOJProblemList data, String content) {
        Pattern p = Pattern.compile("<a href=\"/submit/(\\w+)\" title=");
        Matcher mathcer = p.matcher(content);
        int cnt = 0;
        while (mathcer.find()) {
            cnt++;
            ids.offer(mathcer.group(1));
            System.err.println(ids.size());
        }
        System.err.println(cnt + " ids on " + data);
    }

    @Override
    public void fetchFailed(SPOJProblemList data, Exception reason) {
        System.err.println("Failed fetch of " + data);
        System.err.println(reason.getMessage());
    }

    static class SPOJProblemList implements FetchInfo<SPOJProblemList> {
        private final String url;

        @Override
        public String toString() {
            return "SPOJProblemList{" +
                    "url='" + url + '\'' +
                    '}';
        }

        SPOJProblemList(String url) {
            this.url = url;
        }

        private static final URLContentLoader<SPOJProblemList> LOADER = new StandardURLContentLoader<>();

        @Override
        public URLContentLoader<SPOJProblemList> getLoader() {
            return LOADER;
        }

        @Override
        public SPOJProblemList get() {
            return this;
        }

        @Override
        public String getURL() {
            return url;
        }
    }

}
