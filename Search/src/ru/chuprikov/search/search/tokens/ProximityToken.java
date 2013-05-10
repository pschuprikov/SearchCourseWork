package ru.chuprikov.search.search.tokens;

public class ProximityToken implements Token {
    private final int proximity;

    public ProximityToken(int proximity) {
        this.proximity = proximity;
    }

    public int getProximity() {
        return proximity;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.PROXIMITY;
    }
}
