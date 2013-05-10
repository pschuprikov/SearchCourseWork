package ru.chuprikov.search.search.tokens;

public class PrimitiveToken implements Token {
    private final TokenKind kind;

    public PrimitiveToken(TokenKind kind) {
        this.kind = kind;
    }

    @Override
    public TokenKind kind() {
        return kind;
    }
}
