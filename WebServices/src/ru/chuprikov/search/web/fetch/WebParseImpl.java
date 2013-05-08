package ru.chuprikov.search.web.fetch;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;
import ru.kirillova.search.database.MultiSiteContentParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.web.fetch.WebParse")
public class WebParseImpl implements WebParse {
    private SearchDatabase searchDB;
    private FetchedDB fetchedDB;
    private ParsedDB parsedDB;

    @PostConstruct
    void initDatabaseConnection() {
        try {

            searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            parsedDB = searchDB.openParseDB();
            fetchedDB = searchDB.openFetchDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    void closeDatabaseConnection() {
        try {
            fetchedDB.close();
            parsedDB.close();
            searchDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProcessStatistics parse(ProblemID from, ProblemID to) throws Exception {
        int total = 0;
        int already = 0;
        int success = 0;
        MultiSiteContentParser parser = new MultiSiteContentParser();
        try (CloseableIterator<ProblemRawData> it = fetchedDB.upperBound(from)) {
            while (it.hasNext()) {
                ProblemRawData current = it.next();
                if (current.getProblemID().compareTo(to) > 0)
                    break;
                total++;
                if (parsedDB.contains(current.getProblemID())) {
                    already++;
                } else {
                    try {
                        parsedDB.saveParsed(parser.parseContent(current));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    success++;
                }
            }
        }
        ProcessStatistics stat = new ProcessStatistics();
        stat.setNumAlreadyFetched(already);
        stat.setNumSuccessful(success);
        stat.setNumTotal(total);

        return stat;
    }

    @Override
    public ProcessStatistics parseAll() throws Exception {
        int total = 0;
        int already = 0;
        int success = 0;
        MultiSiteContentParser parser = new MultiSiteContentParser();
        try (CloseableIterator<ProblemRawData> it = fetchedDB.iterator()) {
            while (it.hasNext()) {
                ProblemRawData current = it.next();
                total++;
                if (parsedDB.contains(current.getProblemID())) {
                    already++;
                } else {
                    parsedDB.saveParsed(parser.parseContent(current));
                    success++;
                }
            }
        }
        ProcessStatistics stat = new ProcessStatistics();
        stat.setNumAlreadyFetched(already);
        stat.setNumSuccessful(success);
        stat.setNumTotal(total);

        return stat;
    }

    @Override
    public void clearParsed() throws Exception {
        searchDB.openParseDB();
    }

    @Override
    public ParsedProblem getProblemParsed(ProblemID problemID) {
        return parsedDB.get(problemID);
    }

    @Override
    public ParsedProblem getFirstProblemParsed() throws Exception {
        try (CloseableIterator<ParsedProblem> it = parsedDB.iterator()) {
            return it.hasNext() ? it.next() : null;
        }
    }

    @Override
    public ParsedProblem[] getNextProblemParseds(ProblemID problemID, int count) throws Exception {
        ArrayList<ParsedProblem> result = new ArrayList<>();
        try (CloseableIterator<ParsedProblem> it = parsedDB.upperBound(problemID)) {
            while (it.hasNext() && result.size() < count) {
                result.add(it.next());
            }
        }
        return result.toArray(new ParsedProblem[result.size()]);
    }
}
