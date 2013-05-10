package ru.chuprikov.search.search;

import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.database.IndexDB;
import ru.chuprikov.search.database.SearchDatabase;
import ru.chuprikov.search.database.TermDB;
import ru.chuprikov.search.database.datatypes.Datatypes;
import ru.chuprikov.search.database.datatypes.Term;
import ru.kirillova.search.normspellcorr.Normalize;
import ru.kirillova.search.normspellcorr.Tokenization;

import java.util.*;

public class Search implements AutoCloseable {
    private final IndexDB indexDB;
    private final TermDB termDB;

    public Search(SearchDatabase searchDB) throws Exception {
        this.indexDB = searchDB.openIndexDB(0);
        this.termDB = searchDB.openTermDB();
    }

    List<Datatypes.Posting> intersect(Iterator<Datatypes.Posting> fst, Iterator<Datatypes.Posting> snd, int limit) {
        ArrayList<Datatypes.Posting> result = new ArrayList<>();
        Datatypes.Posting fstTemp = null;
        Datatypes.Posting sndTemp = null;
        while (true) {
            if (!fst.hasNext() && sndTemp == null || !snd.hasNext() && fstTemp == null)
                break;
            if (fstTemp == null) {
                fstTemp = fst.next();
            }
            if (sndTemp == null) {
                sndTemp = snd.next();
            }
            if (fstTemp.getDocumentID() == sndTemp.getDocumentID()) {
                result.add(fstTemp);
                fstTemp = sndTemp = null;
            } else if (fstTemp.getDocumentID() < sndTemp.getDocumentID())
                fstTemp = null;
            else
                sndTemp = null;
            if (result.size() == limit)
                break;
        }
        return result;
    }

    public List<Long> searchAndGetDocIDs(String request, int limit) throws Exception {
        List<String> tokens = Tokenization.getTokens(request);
        ArrayList<Term> terms = new ArrayList<>();
        for (String s : tokens) {
            terms.add(termDB.get(Normalize.getBasisWord(s)));
        }
        Collections.sort(terms, new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                return Long.compare(o1.getCount(), o2.getCount());
            }
        });

        List<Datatypes.Posting> currentResult = new ArrayList<>();
        try (CloseableIterator<Datatypes.Posting> leastFrequentIt = indexDB.iterator(terms.get(0).getTermID())) {
            while (leastFrequentIt.hasNext() && currentResult.size() < limit) {
                currentResult.add(leastFrequentIt.next());
            }
        }
        for (int i = 1; i < terms.size(); i++) {
            try(CloseableIterator<Datatypes.Posting> it = indexDB.iterator(terms.get(i).getTermID())) {
                currentResult = intersect(currentResult.iterator(), it, limit);
            }
        }

        List<Long> result = new ArrayList<>();
        for (Datatypes.Posting post : currentResult) {
            result.add(post.getDocumentID());
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        indexDB.close();
        termDB.close();
    }
}
