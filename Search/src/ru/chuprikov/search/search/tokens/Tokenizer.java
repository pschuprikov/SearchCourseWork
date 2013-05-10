package ru.chuprikov.search.search.tokens;

public class Tokenizer {
    private int cur = 0;
    private final String str;
    private Token currentToken;

    public Tokenizer(String str) {
        this.str = str;
        readNextToken();
    }

    public Token currentToken() {
        return currentToken;
    }

    public void readNextToken() {
        while (cur < str.length() && Character.isSpaceChar(str.charAt(cur)))
            cur++;
        if (cur == str.length()) {
            currentToken = TokensFactory.createEOFToken();
        } else if (str.charAt(cur) == '"') {
            currentToken = TokensFactory.createCiteToken();
            cur++;
        } else if (str.charAt(cur) == '/') {
            int proximity = 0;
            cur++;
            while (cur < str.length() && Character.isDigit(str.charAt(cur))) {
                proximity = proximity * 10 + (str.charAt(cur) - '0');
                cur++;
            }
            currentToken = TokensFactory.createProximityToken(proximity);
        } else {
            StringBuilder word = new StringBuilder();
            while (cur < str.length() && Character.isAlphabetic(str.charAt(cur))) {
                word.append(str.charAt(cur));
                cur++;
            }
            currentToken = TokensFactory.createWordToken(word.toString());
        }
    }
}
