package ru.chuprikov.search.gather.problemsets;

import ru.chuprikov.search.gather.ProblemRawData;
import ru.chuprikov.search.gather.fetcher.FetchInfo;
import ru.chuprikov.search.gather.fetcher.StandardURLContentLoader;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Proxy;
import java.net.URLConnection;
import java.net.URLEncoder;

class UVaURLContentLoader extends StandardURLContentLoader<ProblemFetchInfo> {

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
    public String loadContent(FetchInfo<ProblemFetchInfo> problem, Proxy proxy) throws IOException {
        if (!problem.get().resource.equals("UVa"))
            throw new AssertionError();

        StringBuilder sb = new StringBuilder();
        sb.append("<<<").append(readFromUVa(problem.get(), proxy)).append(">>>").append(super.loadContent(problem, proxy));
        return sb.toString();
    }
}
