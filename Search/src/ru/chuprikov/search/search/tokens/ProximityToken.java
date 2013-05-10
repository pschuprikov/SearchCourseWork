package ru.chuprikov.search.search.tokens;

class ProximityToken implements Token {
    private final int proximity;

    public ProximityToken(int proximity) {
        this.proximity = proximity;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.PROXIMITY;
    }

    @Override
    public int getIntegerValue() {
        return proximity;
    }

    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException();
    }
}
