package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.DatabaseEntry;

abstract class AbstractBerkeleyDB {

    final ThreadLocal<DatabaseEntry> keyEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
    final ThreadLocal<DatabaseEntry> valueEntry = new ThreadLocal<DatabaseEntry>() {
        @Override
        protected DatabaseEntry initialValue() {
            return new DatabaseEntry();
        }
    };
}
