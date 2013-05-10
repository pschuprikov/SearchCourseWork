package ru.chuprikov.search.search.tokens;

public class WordToken implements Token {
    public String getWord() {
        return word;
    }

    private final String word;

    public WordToken(String word) {
        this.word = word;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.WORD;
    }
}
