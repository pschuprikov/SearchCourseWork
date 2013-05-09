package ru.chuprikov.search.web.documents;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.DocumentDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.datatypes.Document;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.web.documents.WebDocumentDB")
public class WebDocumentDBImpl implements WebDocumentDB {
    private SearchDatabase searchDB;
    private DocumentDB documentDB;

    @PostConstruct
    void openDatabase() {
        try {
            searchDB = SearchDatabases.openBerkeley(new File("/home/pasha/repos/SearchCourseWork/mydb/"));
            documentDB = searchDB.openDocumentDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    void closeDatabase() {
        try {
            documentDB.close();
            searchDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Document getDocument(long id) throws Exception {
        return documentDB.get(id);
    }

    @Override
    public Document getFirstDocument() throws Exception {
        try (CloseableIterator<Document> it = documentDB.iterator()) {
            return it.hasNext() ?  it.next() : null;
        }
    }

    @Override
    public Document[] getNextDocuments(long documentID, int length) throws Exception {
        ArrayList<Document> result = new ArrayList<>();
        try (CloseableIterator<Document> it = documentDB.upperBound(documentID)) {
            while (it.hasNext() && result.size() < length) {
                result.add(it.next());
            }
        }
        return result.toArray(new Document[result.size()]);
    }
}
