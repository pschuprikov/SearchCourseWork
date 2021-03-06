package ru.chuprikov.search.web.terms;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.TermDB;
import ru.chuprikov.search.datatypes.Term;
import ru.chuprikov.search.web.WebTermDB;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.web.WebTermDB")
public class WebTermDBImpl implements WebTermDB {

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
    public Term get(String term) throws Exception {
        return termDB.get(term);
    }

    @Override
    public Term getFirstTerm() throws Exception {
        try (CloseableIterator<String> it = termDB.iterator()) {
            return it.hasNext() ? get(it.next()) : null;
        }
    }

    @Override
    public Term[] getNextTerms(String term, int length) {
        ArrayList<Term> result = new ArrayList<>();
        try (CloseableIterator<String> it = termDB.upperBound(term)) {
            while (it.hasNext() && result.size() < length)
                result.add(get(it.next()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toArray(new Term[result.size()]);
    }
}