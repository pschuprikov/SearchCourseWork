package ru.chuprikov.search.database;

import ru.chuprikov.search.database.datatypes.ProblemID;
import ru.chuprikov.search.database.datatypes.ProblemRawData;

import javax.jws.WebService;

@WebService
public interface FetchedDB extends AutoCloseable {
    void saveFetched(ProblemRawData problem);
    boolean contains(ProblemID problemID);

    ProblemRawData get(ProblemID problemID);
    CloseableListIterator<ProblemRawData> iterator();
    CloseableListIterator<ProblemRawData> upperBound(ProblemID problemID);
}
