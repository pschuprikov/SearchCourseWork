package ru.chuprikov.search.search.tokens;

public interface Token {
    TokenKind kind();
    int getIntegerValue();
    String getStringValue();
}
