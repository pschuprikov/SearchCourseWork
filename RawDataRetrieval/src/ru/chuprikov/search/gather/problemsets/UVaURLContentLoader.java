package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.ProblemRawData;
import ru.chuprikov.search.gather.loader.ProblemFetchInfo;
import ru.chuprikov.search.gather.loader.StandardURLContentLoader;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Proxy;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: pasha
 * Date: 4/28/13
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
class UVaURLContentLoader extends StandardURLContentLoader {

    private static final String request = "http://acm.uva.es/local/online_judge/gotosearch_uva.php";

    private String readFromUVa(ProblemRawData problem, Proxy proxy) throws IOException {
        URLConnection connection = getConnection(request, proxy);
        connection.setDoOutput(true);

        String data = URLEncoder.encode("p", "UTF-8") + "=" + URLEncoder.encode(problem.problemID, "UTF-8");
        data += "&" + URLEncoder.encode("info", "UTF-8") + "=" + URLEncoder.encode("Info", "UTF-8");

        try (OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream())) {
            wr.write(data);
            wr.flush();
            return readFromConnection(connection);
        }
    }

    @Override
    public String loadContent(ProblemFetchInfo problem, Proxy proxy) throws IOException {
        if (!problem.resource.equals("UVa"))
            throw new AssertionError();

        StringBuilder sb = new StringBuilder();
        sb.append("<<<").append(readFromUVa(problem, proxy)).append(">>>").append(super.loadContent(problem, proxy));
        return sb.toString();
    }
}
