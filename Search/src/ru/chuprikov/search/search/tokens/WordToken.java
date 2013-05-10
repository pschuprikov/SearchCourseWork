package ru.chuprikov.search.search.tokens;

class WordToken implements Token {
    private final String word;

    public WordToken(String word) {
        this.word = word;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.WORD;
    }

    @Override
    public int getIntegerValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringValue() {
        return word;
    }
}
