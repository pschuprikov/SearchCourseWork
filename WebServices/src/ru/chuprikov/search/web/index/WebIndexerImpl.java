package ru.chuprikov.search.web.index;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.datatypes.*;
import ru.chuprikov.search.index.indexer.Indexer;
import ru.chuprikov.search.index.indexer.spimi.SPIMIIndexer;
import ru.chuprikov.search.web.WebIndexer;
import ru.kirillova.search.normspellcorr.Normalize;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebService(endpointInterface = "ru.chuprikov.search.web.WebIndexer")
public class WebIndexerImpl implements WebIndexer {
    private SearchDatabase searchDB;
    private DocumentDB documentDB;
    private TermDB termDB;
    private ParsedDB parsedDB;
    private BigrammDB bigrammDB;

    private static void splitWithType(Map<String, Datatypes.Posting.Builder> dic, String text, long docID, Datatypes.Posting.PositionType type) {
        int positionIdx = 0;
        for (String s : Normalize.getNormalTokens(text)) {
            if (!s.isEmpty()) {
                if (!dic.containsKey(s)) {
                    dic.put(s, Datatypes.Posting.newBuilder());
                    dic.get(s).setDocumentID(docID);
                }
                dic.get(s).addPositions(Datatypes.Posting.Position.newBuilder().setIndex(positionIdx++).setType(type).build());
            }
        }
    }

    @PostConstruct
    private void openDatabaseConnection() throws Exception {
        searchDB = SearchDatabases.openBerkeley(new File("/home/pasha/repos/SearchCourseWork/mydb/"));
        documentDB = searchDB.openDocumentDB();
        termDB = searchDB.openTermDB();
        parsedDB = searchDB.openParsedDB();
        bigrammDB = searchDB.openBigrammDB();
    }

    @PreDestroy
    private void closeDatabaseConnection() throws Exception {
        termDB.close();
        bigrammDB.close();
        documentDB.close();
        parsedDB.close();
        searchDB.close();
    }

    @Override
    public ProcessStatistics index(ProblemID from, ProblemID to, long maxMemoryUsage, int maxPostingsChunkSize) throws Exception {
        int total = 0;
        int successful = 0;
        try (IndexDB indexDB = searchDB.openIndexDB(maxPostingsChunkSize);
             Indexer indexer = new SPIMIIndexer(new File(System.getProperty("user.dir") + "/spimi"), indexDB, termDB, bigrammDB, maxMemoryUsage);
             CloseableIterator<ParsedProblem> parsedIter = parsedDB.upperBound(from)) {

            while (parsedIter.hasNext()) {
                final ParsedProblem parsedProblem = parsedIter.next();
                if (parsedProblem.getProblemID().compareTo(to) > 0)
                    break;

                total++;
                successful = processDocument(successful, indexer, parsedProblem);
            }
        }
        return new ProcessStatistics(total, successful, 0);
    }

    private int processDocument(int successful, Indexer indexer, ParsedProblem parsedProblem) throws IOException {
        Map<String, Datatypes.Posting.Builder> documentPostings = new HashMap<>();
        try {
            final long documentID = documentDB.addDocument(new Document(parsedProblem.getProblemID(), parsedProblem.getUrl()));
            for (Datatypes.Posting.PositionType positionType : Datatypes.Posting.PositionType.values())
                splitWithType(documentPostings, parsedProblem.getBlock(positionType), documentID, positionType);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return successful;
        }

        successful++;
        for (Map.Entry<String, Datatypes.Posting.Builder> e : documentPostings.entrySet()) {
            indexer.addToIndex(e.getKey(), e.getValue().build());
        }
        return successful;
    }

    @Override
    public ProcessStatistics indexAll(long maxMemoryUsage, int maxPostingsChunkSize) throws Exception {
        int total = 0;
        int successful = 0;
        try (IndexDB indexDB = searchDB.openIndexDB(maxPostingsChunkSize);
             Indexer indexer = new SPIMIIndexer(new File(System.getProperty("user.dir") + "/spimi"), indexDB, termDB, bigrammDB, maxMemoryUsage);
             CloseableIterator<ParsedProblem> parsedIter = parsedDB.iterator()) {

            while (parsedIter.hasNext()) {
                total++;
                successful = processDocument(successful, indexer, parsedIter.next());
            }
        }
        return new ProcessStatistics(total, successful, 0);
    }
}