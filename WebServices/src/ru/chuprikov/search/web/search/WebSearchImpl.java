package ru.chuprikov.search.web.search;

import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.datatypes.SearchResponse;
import ru.chuprikov.search.search.Search;
import ru.chuprikov.search.web.WebSearch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;

@WebService(endpointInterface = "ru.chuprikov.search.web.WebSearch")
public class WebSearchImpl implements WebSearch {
    private Search search;
    private SearchDatabase searchDB;

    @PostConstruct
    void openSearch() throws Exception {
        searchDB = SearchDatabases.openBerkeley(new File("/home/pasha/repos/SearchCourseWork/mydb/"));
        search = new Search(searchDB);
        System.err.println("Search started");
    }

    @PreDestroy
    void closeSearch() throws Exception {
        search.close();
        searchDB.close();
    }

    @Override
    public SearchResponse searchSimpleConjunction(String request, int limit) throws Exception {
        return search.searchAndGetDocIDs(request, limit);
    }
}
