package ru.chuprikov.search.search.tokens;

public class TokensFactory {
    private static final Token eof = new PrimitiveToken(TokenKind.EOF);
    private static final Token cite = new PrimitiveToken(TokenKind.CITE);

    public static Token createEOFToken() {
        return eof;
    }

    public static Token createCiteToken() {
        return cite;
    }

    public static Token createWordToken(String word) {
        return new WordToken(word);
    }

    public static Token createProximityToken(int proximity) {
        return new ProximityToken(proximity);
    }
}
