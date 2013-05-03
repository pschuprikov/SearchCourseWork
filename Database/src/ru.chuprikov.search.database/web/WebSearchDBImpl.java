package ru.chuprikov.search.database.web;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.TermDB;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.database.web.WebSearchDB")
public class WebSearchDBImpl implements WebSearchDB {
    private SearchDatabase searchDB;
    private TermDB termDB;

    @PostConstruct
    void openDatabase() {
        try {
            searchDB = SearchDatabases.openBerkeley(new File("/home/pasha/repos/SearchCourseWork/mydb/"));
            termDB = searchDB.openTermDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    void closeDatabase() {
        try {
            termDB.close();
            searchDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long size() {
        try {
            return termDB.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public TermInfo getTermInfo(String term) {
        TermInfo result = new TermInfo();
        try {
            result.setTerm(term);
            result.setId(termDB.getID(term));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public TermInfo getFirstTermInfo() {
        TermInfo result = new TermInfo();
        try (CloseableIterator<String> iter = termDB.iterator()) {
            result.setTerm(iter.next());
            result.setId(termDB.getID(result.getTerm()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public TermInfo[] getNextTermInfos(String str, int length) {
        ArrayList<TermInfo> result = new ArrayList<>();
        try (CloseableIterator<String> it = termDB.upperBound(str)) {
            while (it.hasNext() && result.size() < length) {
                TermInfo ti = new TermInfo();
                ti.setTerm(it.next());
                ti.setId(termDB.getID(ti.getTerm()));
                result.add(ti);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        TermInfo[] resultArray =  result.toArray(new TermInfo[result.size()]);
        return resultArray;
    }
}