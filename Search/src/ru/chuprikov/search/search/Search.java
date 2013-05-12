package ru.chuprikov.search.search;

import ru.chuprikov.search.database.*;
import ru.chuprikov.search.datatypes.Datatypes;
import ru.chuprikov.search.datatypes.Document;
import ru.chuprikov.search.datatypes.SearchResponse;
import ru.chuprikov.search.datatypes.Term;
import ru.chuprikov.search.search.joiners.Joiners;
import ru.chuprikov.search.search.tokens.TokenKind;
import ru.chuprikov.search.search.tokens.Tokenizer;
import ru.kirillova.search.normspellcorr.Kgramm;
import ru.kirillova.search.normspellcorr.Normalize;

import java.util.*;

public class Search implements AutoCloseable {
    private final IndexDB indexDB;
    private final TermDB termDB;
    private final DocumentDB documentDB;
    private final BigrammDB bigrammDB;
    private final Kgramm kgramm;

    public Search(SearchDatabase searchDB) throws Exception {
        this.documentDB = searchDB.openDocumentDB();
        this.indexDB = searchDB.openIndexDB(0);
        this.termDB = searchDB.openTermDB();
        this.bigrammDB = searchDB.openBigrammDB();
        this.kgramm = new Kgramm(termDB, bigrammDB);
    }

    private Iterator<PostingInfo> parseE(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators, StringBuilder correctedRequestBuilder) throws Exception {
        Iterator<PostingInfo> cur = parseN(tokenizer, openedIterators, correctedRequestBuilder);
        while (tokenizer.currentToken().kind() == TokenKind.PROXIMITY) {
            int proximity = tokenizer.currentToken().getIntegerValue();
            correctedRequestBuilder.append(" /" + proximity + " ");
            tokenizer.readNextToken();
            Iterator<PostingInfo> next = parseN(tokenizer, openedIterators, correctedRequestBuilder);
            cur = new Merger(cur, next, Joiners.getProximityJoiner(proximity));
        }
        return cur;
    }

    private Iterator<PostingInfo> getPostingsIterator(String token, List<CloseableIterator<Datatypes.Posting>> openedIterators, StringBuilder correctedRequestBuilder) throws Exception {
        final String termStr = Normalize.getBasisWord(token);
        Term term = termDB.get(termStr);

        if (term == null) {
            String tokenReplacement = kgramm.fixMistake(token).get(0);
            term = termDB.get(Normalize.getBasisWord(tokenReplacement));
            correctedRequestBuilder.append(tokenReplacement);
        } else {
            correctedRequestBuilder.append(token);
        }

        final long termID = term.getTermID();
        final CloseableIterator<Datatypes.Posting> termPostingsIterator = indexDB.iterator(termID);
        openedIterators.add(termPostingsIterator);
        return new PostingsAdapter(termPostingsIterator);
    }

    private Iterator<PostingInfo> parseN(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators, StringBuilder correctedRequestBuilder) throws Exception {
        Iterator<PostingInfo> result;
        if (tokenizer.currentToken().kind() == TokenKind.CITE) {
            correctedRequestBuilder.append('"');
            tokenizer.readNextToken();

            result = getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators, correctedRequestBuilder);
            tokenizer.readNextToken();

            while (tokenizer.currentToken().kind() != TokenKind.CITE) {
                correctedRequestBuilder.append(' ');
                result = new Merger(result, getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators, correctedRequestBuilder), Joiners.getPhraseJoiner());
                tokenizer.readNextToken();
            }

            correctedRequestBuilder.append('"');
            tokenizer.readNextToken();
        } else {
            result = getPostingsIterator(tokenizer.currentToken().getStringValue(), openedIterators, correctedRequestBuilder);
            tokenizer.readNextToken();
        }
        return result;
    }

    private Iterator<PostingInfo> parseC(Tokenizer tokenizer, List<CloseableIterator<Datatypes.Posting>> openedIterators, StringBuilder correctedRequestBuilder) throws Exception {
        Iterator<PostingInfo> cur = parseE(tokenizer, openedIterators, correctedRequestBuilder);
        while (tokenizer.currentToken().kind() != TokenKind.EOF) {
            correctedRequestBuilder.append(' ');
            cur = new Merger(cur, parseE(tokenizer, openedIterators, correctedRequestBuilder), Joiners.getOrJoiner());
        }

        return cur;
    }

    public SearchResponse searchAndGetDocIDs(String request, int limit) throws Exception {
        ArrayList<CloseableIterator<Datatypes.Posting>> openedIterators = new ArrayList<>();
        try {
            long startTime = System.currentTimeMillis();

            StringBuilder correctedRequestBuilder = new StringBuilder();
            Iterator<PostingInfo> it = parseC(new Tokenizer(request), openedIterators, correctedRequestBuilder);
            Set<Long> duplicateEliminator = new HashSet<>();

            while (it.hasNext() && duplicateEliminator.size() < limit)
                duplicateEliminator.add(it.next().getDocumentID());

            Document[] found = new Document[duplicateEliminator.size()];
            int writeIdx = 0;
            for (long documentID : duplicateEliminator)
                found[writeIdx++] = documentDB.get(documentID);

            return new SearchResponse(System.currentTimeMillis() - startTime, correctedRequestBuilder.toString(), found);
        } finally {
            for (CloseableIterator<Datatypes.Posting> cit : openedIterators)
                cit.close();
        }
    }

    @Override
    public void close() throws Exception {
        documentDB.close();
        indexDB.close();
        bigrammDB.close();
        termDB.close();
    }
}
