package ru.chuprikov.search.web;


import ru.chuprikov.search.web.fetch.WebFetchImpl;
import ru.chuprikov.search.web.terms.WebTermDBImpl;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    private static Runnable getTermPublisher(final URL u) {
        return new Runnable() {
            @Override
            public void run() {
                Endpoint.publish(u.toExternalForm(), new WebTermDBImpl());
            }
        };
    }

    private static Runnable getDocumentPublisher(final URL u) {
        return new Runnable() {
            @Override
            public void run() {

                Endpoint.publish(u.toExternalForm(), new WebDocumentDBImpl());
            }
        };
    }

    private static Runnable getFetchPublisher(final URL u) {
        return new Runnable() {
            @Override
            public void run() {
                Endpoint.publish(u.toExternalForm(), new WebFetchImpl());
            }
        };
    }

    public static void main(String[] args) {
        Executor exec = Executors.newCachedThreadPool();
        try {
            final URL termsURL = new URL("http://localhost:8081/WS/terms");
            final URL documentsURL = new URL("http://localhost:8081/WS/documents");
            final URL fetchURL = new URL("http://localhost:8081/WS/fetch");
            exec.execute(getTermPublisher(termsURL));
            Thread.sleep(1000);
            exec.execute(getDocumentPublisher(documentsURL));
            Thread.sleep(1000);
            exec.execute(getFetchPublisher(fetchURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
