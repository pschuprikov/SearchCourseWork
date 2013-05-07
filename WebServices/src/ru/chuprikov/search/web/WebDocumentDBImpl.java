package ru.chuprikov.search.web;

import ru.chuprikov.search.database.DocumentDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.datatypes.Datatypes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

@WebService(endpointInterface = "ru.chuprikov.search.web.WebDocumentDB")
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
    public DocumentInfo getDocumentInfo(long id) {
        DocumentInfo result = new DocumentInfo();
        try {
            Datatypes.Document document = documentDB.getDocument(id);
            result.setId(id);
            result.setProblemID(document.getProblemid());
            result.setResource(document.getResource());
            result.setUrl(document.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public DocumentInfo getFirstDocumentInfo() {
        DocumentInfo result = new DocumentInfo();
        try {
            return getDocumentInfo(documentDB.iterator().next());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public DocumentInfo[] getNextDocumentInfos(DocumentInfo documentInfo, int length) {
        ArrayList<DocumentInfo> result = new ArrayList<>();
        try {
            for (Iterator<Long> it = documentDB.upperBound(documentInfo.getId()); it.hasNext() && result.size() < length;) {
                result.add(getDocumentInfo(it.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toArray(new DocumentInfo[result.size()]);
    }
}
