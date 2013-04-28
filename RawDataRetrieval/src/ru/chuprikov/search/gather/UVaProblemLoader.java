package ru.chuprikov.search.gather;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class UVaProblemLoader extends StandardProblemLoader {

    private static String request = "http://acm.uva.es/local/online_judge/gotosearch_uva.php";

    private String readFromUVa(ProblemRawData problem, Proxy proxy, String info) throws IOException {
        URLConnection connection = getConnection(request, proxy);
        connection.setDoOutput(true);

        String data = URLEncoder.encode("p", "UTF-8") + "=" + URLEncoder.encode(problem.problemID, "UTF-8");
        data += "&" + URLEncoder.encode("info", "UTF-8") + "=" + URLEncoder.encode(info, "UTF-8");

        try (OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream())) {
            wr.write(data);
            wr.flush();
            return readFromConnection(connection);
        }
    }

    @Override
    public String load(ProblemRawData problem, Proxy proxy) throws IOException {
        if (problem.resource != "UVa")
            throw new AssertionError();

        StringBuilder sb = new StringBuilder();
        sb.append("<<<").append(readFromUVa(problem, proxy, "Info")).append(">>>").append(super.load(problem, proxy));
        return sb.toString();
    }
}
