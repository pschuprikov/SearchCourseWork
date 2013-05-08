package ru.chuprikov.search.web.index;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.SearchDatabases;
import ru.chuprikov.search.database.datatypes.Datatypes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import java.io.File;
import java.util.ArrayList;

@WebService(endpointInterface = "ru.chuprikov.search.web.index.WebIndexDB")
public class WebIndexDBImpl implements WebIndexDB{
    private SearchDatabase searchDB;
    private IndexDB indexDB;

    @PostConstruct
    private void openDatabaseConnection() throws Exception {
        searchDB = SearchDatabases.openBerkeley(new File(System.getProperty("user.dir") + "/mydb"));
        indexDB = searchDB.openIndexDB(0);
    }

    @PreDestroy
    private void closeDatabaseConnection() throws Exception {
        indexDB.close();
        searchDB.close();
    }

    @Override
    public byte[] readFirstPosting(long termID) throws Exception {
        try(CloseableIterator<Datatypes.Posting> it = indexDB.iterator(termID)) {
            return it.hasNext() ? it.next().toByteArray() : null;
        }
    }

    @Override
    public byte[][] readNextPostings(long termID, long documentID, int count) throws Exception {
        ArrayList<byte[]> result = new ArrayList<>();
        try (CloseableIterator<Datatypes.Posting> it = indexDB.upperBound(termID, documentID)) {
            while (it.hasNext() && result.size() < count) {
                result.add(it.next().toByteArray());
            }
        }
        return result.toArray(new byte[result.size()][]);
    }
}
