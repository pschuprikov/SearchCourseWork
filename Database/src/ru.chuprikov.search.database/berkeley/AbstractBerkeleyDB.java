package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.DatabaseEntry;

public abstract class AbstractBerkeleyDB {

    protected final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
    protected final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
}
