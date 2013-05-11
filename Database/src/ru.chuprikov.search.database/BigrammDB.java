package ru.chuprikov.search.database;

import ru.chuprikov.search.datatypes.BigrammUnit;

public interface BigrammDB extends AutoCloseable {
    void add(String bi, BigrammUnit unit);
    BigrammUnit[] get(String bi) throws Exception;
}
