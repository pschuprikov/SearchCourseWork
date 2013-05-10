package ru.chuprikov.search.search;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.database.datatypes.Datatypes;
import ru.chuprikov.search.database.datatypes.Document;
import ru.chuprikov.search.search.joiners.Joiners;
import ru.chuprikov.search.search.tokens.TokenKind;
import ru.chuprikov.search.search.tokens.Tokenizer;
import ru.kirillova.search.normspellcorr.Normalize;

import java.util.*;

public class Search implements AutoCloseable {
    private final IndexDB indexDB;
    private final TermDB termDB;
    private final DocumentDB documentDB;

    public Search(SearchDatabase searchDB) throws Exception {
        this.documentDB = searchDB.openDocumentDB();
        this.indexDB = searchDB.openIndexDB(0);
        this.termDB = searchDB.openTermDB();
    }

    private Iterator<PostingInfo> parseE(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators) throws Exception {
        Iterator<PostingInfo> cur = parseN(tokenizer, openedIterators);
        while (tokenizer.currentToken().kind() == TokenKind.PROXIMITY) {
            int proximity = tokenizer.currentToken().getIntegerValue();
            tokenizer.readNextToken();
            Iterator<PostingInfo> next = parseN(tokenizer, openedIterators);
            cur = new Merger(cur, next, Joiners.getProximityJoiner(proximity));
        }
        return cur;
    }

    private Iterator<PostingInfo> getPostingsIterator(String token, List<CloseableIterator<Datatypes.Posting>> openedIterators) throws Exception {
        final String term = Normalize.getBasisWord(token);
        final long termID = termDB.get(term).getTermID();
        final CloseableIterator<Datatypes.Posting> termPostingsIterator = indexDB.iterator(termID);
        openedIterators.add(termPostingsIterator);
        return new PostingsAdapter(termPostingsIterator);
    }

    private Iterator<PostingInfo> parseN(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators) throws Exception {
        Iterator<PostingInfo> result;
        if (tokenizer.currentToken().kind() == TokenKind.CITE) {
            tokenizer.readNextToken();

            result = getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators);
            tokenizer.readNextToken();

            while (tokenizer.currentToken().kind() != TokenKind.CITE) {
                result = new Merger(result, getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators), Joiners.getPhraseJoiner());
                tokenizer.readNextToken();
            }

            tokenizer.readNextToken();
        } else {
            result = getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators);
            tokenizer.readNextToken();
        }
        return result;
    }

    private Iterator<PostingInfo> parseC(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators) throws Exception {
        Iterator<PostingInfo> cur = parseE(tokenizer, openedIterators);
        while (tokenizer.currentToken().kind() != TokenKind.EOF)
            cur = new Merger(cur, parseE(tokenizer, openedIterators), Joiners.getOrJoiner());

        return cur;
    }

    public List<Document> searchAndGetDocIDs(String request, int limit) throws Exception {
        ArrayList<CloseableIterator<Datatypes.Posting>> openedIterators = new ArrayList<>();
        try {
        Iterator<PostingInfo> it = parseC(new Tokenizer(request), openedIterators);
        Set<Long> duplicateEliminator = new HashSet<>();

        while (it.hasNext() && duplicateEliminator.size() < limit)
            duplicateEliminator.add(it.next().getDocumentID());

        List<Document> result = new ArrayList<>();
            for (long documentID : duplicateEliminator)
                result.add(documentDB.get(documentID));
        return result;
        } finally {
            for (CloseableIterator<Datatypes.Posting> cit : openedIterators)
                cit.close();
        }
    }

    @Override
    public void close() throws Exception {
        documentDB.close();
        indexDB.close();
        termDB.close();
    }
}
