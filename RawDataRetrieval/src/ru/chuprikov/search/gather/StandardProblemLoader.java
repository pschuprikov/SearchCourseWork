package ru.chuprikov.search.gather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/27/13
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
class StandardProblemLoader implements ProblemLoader {

    protected static int CONNECT_TIMEOUT_MILLS = 500;
    protected static int READ_TIMEOUT_MILLS = 500;

    protected String readFromConnection(URLConnection conn) throws IOException {
        final BufferedReader contentReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder contentBuilder = new StringBuilder();
        while (true) {
            final String line = contentReader.readLine();
            if (line == null)
                break;
            contentBuilder.append(line).append("\n");
        }
        return contentBuilder.toString();
    }

    protected URLConnection getConnection(String urlString, Proxy proxy) throws IOException {
        URLConnection conn = new URL(urlString).openConnection(proxy);

        conn.setConnectTimeout(CONNECT_TIMEOUT_MILLS);
        conn.setReadTimeout(READ_TIMEOUT_MILLS);
        return conn;
    }

    @Override
    public String load(ProblemRawData problem, Proxy proxy) throws IOException {
        return readFromConnection(getConnection(problem.url, proxy));
    }
}
