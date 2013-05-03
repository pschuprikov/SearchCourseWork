package ru.chuprikov.search.index;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.database.datatypes.Datatypes;
import ru.chuprikov.search.database.datatypes.ParsedProblem;
import ru.chuprikov.search.index.indexer.Indexer;
import ru.chuprikov.search.index.indexer.spimi.SPIMIIndexer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static void splitWithType(Map<String, Datatypes.Posting.Builder> dic, String text, long docID, Datatypes.Posting.PositionType type) {
        int positionIdx = 0;
        for (String s : text.split("[\\W]")) {
            if (!s.isEmpty()) {
                if (!dic.containsKey(s)) {
                    dic.put(s, Datatypes.Posting.newBuilder());
                    dic.get(s).setDocumentID(docID);
                }
                dic.get(s).addPositions(Datatypes.Posting.Position.newBuilder().setIndex(positionIdx++).setType(type).build());
            }
        }
    }

    public static void main(String[] args) {
        try(
            SearchDatabase searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
            ParsedDB parsedDB = searchDB.openParseDB();
            IndexDB indexDB = searchDB.openIndexDB(1000000);
            TermDB termDB = searchDB.openTermDB();
            DocumentDB documentDB = searchDB.openDocumentDB();
            CloseableIterator<ParsedProblem> parsedIter = parsedDB.openIterator();
            Indexer indexer = new SPIMIIndexer(new File(System.getProperty("user.dir") + "/spimi"), indexDB, termDB, 1000000);
        ) {
            while (parsedIter.hasNext()) {
                ParsedProblem parsedProblem = parsedIter.next();
                Map<String, Datatypes.Posting.Builder> documentPostings = new HashMap<>();

                try {
                    long documentID = documentDB.addDocument(Datatypes.Document.newBuilder().
                            setProblemid(parsedProblem.problemID).setResource(parsedProblem.resource)
                            .setUrl(parsedProblem.url).build());
                    splitWithType(documentPostings, parsedProblem.title, documentID, Datatypes.Posting.PositionType.TITLE);
                    splitWithType(documentPostings, parsedProblem.inputSpecification, documentID, Datatypes.Posting.PositionType.INPUT_SPEC);
                    splitWithType(documentPostings, parsedProblem.outputSpecification, documentID, Datatypes.Posting.PositionType.OUTPUT_SPEC);
                    splitWithType(documentPostings, parsedProblem.condition, documentID, Datatypes.Posting.PositionType.PLAIN_TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Map.Entry<String, Datatypes.Posting.Builder> e : documentPostings.entrySet()) {
                    indexer.addToIndex(e.getKey(), e.getValue().build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}