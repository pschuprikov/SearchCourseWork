package ru.chuprikov.search.web;


import ru.chuprikov.search.web.documents.WebDocumentDBImpl;
import ru.chuprikov.search.web.fetch.WebFetchImpl;
import ru.chuprikov.search.web.fetch.WebParseImpl;
import ru.chuprikov.search.web.index.WebIndexDBImpl;
import ru.chuprikov.search.web.index.WebIndexerImpl;
import ru.chuprikov.search.web.terms.WebTermDBImpl;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final ArrayList<Endpoint> endpoints = new ArrayList<>();
    private static Runnable getPublisher(final Endpoint endpoint, final URL url) {
        endpoints.add(endpoint);
        return new Runnable() {
            @Override
            public void run() {
                endpoint.publish(url.toExternalForm());
            }
        };
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            final URL termsURL = new URL("http://localhost:8081/WS/terms");
            final URL documentsURL = new URL("http://localhost:8081/WS/documents");
            final URL fetchURL = new URL("http://localhost:8081/WS/fetch");
            final URL parseURL = new URL("http://localhost:8081/WS/parse");
            final URL indexerURL = new URL("http://localhost:8081/WS/index");
            final URL indexURL = new URL("http://localhost:8081/WS/indexdb");

            exec.execute(getPublisher(Endpoint.create(new WebTermDBImpl()), termsURL));
            Thread.sleep(1000);
            exec.execute(getPublisher(Endpoint.create(new WebDocumentDBImpl()), documentsURL));
            Thread.sleep(1000);
            exec.execute(getPublisher(Endpoint.create(new WebFetchImpl()), fetchURL));
            Thread.sleep(1000);
            exec.execute(getPublisher(Endpoint.create(new WebParseImpl()), parseURL));
            Thread.sleep(1000);
            exec.execute(getPublisher(Endpoint.create(new WebIndexerImpl()), indexerURL));
            Thread.sleep(1000);
            exec.execute(getPublisher(Endpoint.create(new WebIndexDBImpl()), indexURL));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.next().equals("exit"))
                    break;
            }

            exec.shutdown();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            for (Endpoint ep : endpoints) {
                ep.stop();
            }
        }
    }
}
