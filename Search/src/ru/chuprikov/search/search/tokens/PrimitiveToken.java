package ru.chuprikov.search.search.tokens;

class PrimitiveToken implements Token {
    private final TokenKind kind;

    public PrimitiveToken(TokenKind kind) {
        this.kind = kind;
    }

    @Override
    public TokenKind kind() {
        return kind;
    }

    @Override
    public int getIntegerValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException();
    }
}
