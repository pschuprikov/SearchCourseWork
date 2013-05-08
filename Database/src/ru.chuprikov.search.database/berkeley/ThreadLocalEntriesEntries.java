package ru.chuprikov.search.database.berkeley;

import com.sleepycat.je.DatabaseEntry;

abstract class ThreadLocalEntriesEntries {

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
