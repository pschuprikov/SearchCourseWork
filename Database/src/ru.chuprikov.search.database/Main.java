package ru.chuprikov.search.database;


import ru.chuprikov.search.database.web.WebSearchDBImpl;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    private static Runnable publisher(final URL u) {
        return new Runnable() {
            @Override
            public void run() {
                Endpoint.publish(u.toExternalForm(), new WebSearchDBImpl());
            }
        };
    }
    public static void main(String[] args) {
        Executor exec = Executors.newCachedThreadPool();
        try {
            URL myserv = new URL("http://localhost:8081/WS/db");
            exec.execute(publisher(myserv));
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
