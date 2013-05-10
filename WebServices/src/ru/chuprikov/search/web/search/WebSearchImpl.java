package ru.chuprikov.search.web.search;

import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.search.Search;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.List;

@WebService(endpointInterface = "ru.chuprikov.search.web.search.WebSearch")
public class WebSearchImpl implements WebSearch {
    private Search search;
    private SearchDatabase searchDB;

    @PostConstruct
    void openSearch() throws Exception {
        searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
        search = new Search(searchDB);
        System.err.println("Search started");
    }

    @PreDestroy
    void closeSearch() throws Exception {
        search.close();
        searchDB.close();
    }

    @Override
    public Long[] searchSimpleConjunction(String request, int limit) throws Exception {
        List<Long> resultPostings = search.searchAndGetDocIDs(request, limit);
        return resultPostings.toArray(new Long[resultPostings.size()]);
    }
}
