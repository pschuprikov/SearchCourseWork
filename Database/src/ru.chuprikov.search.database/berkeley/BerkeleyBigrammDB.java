package ru.chuprikov.search.database.berkeley;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.StoredValueSet;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import ru.chuprikov.search.database.BigrammDB;
import ru.chuprikov.search.database.CloseableIterator;
import ru.chuprikov.search.datatypes.BigrammUnit;

import java.util.ArrayList;

class BerkeleyBigrammDB implements BigrammDB {
    private final Database bigrammDB;
    private final StoredSortedMap<String, BigrammUnit> storedMap;

    BerkeleyBigrammDB(Environment env) {
        bigrammDB = env.openDatabase(null, "bigramm", new DatabaseConfig().setAllowCreate(true).setSortedDuplicates(true));
        storedMap = new StoredSortedMap<>(bigrammDB, new StringBinding(), bigrammBinding, true);
    }

    private static final TupleBinding<BigrammUnit> bigrammBinding = new TupleBinding<BigrammUnit>() {
        @Override
        public BigrammUnit entryToObject(TupleInput input) {
            return new BigrammUnit(input.readLong(), input.readInt());
        }

        @Override
        public void objectToEntry(BigrammUnit object, TupleOutput output) {
            output.writeLong(object.getTermID()).writeInt(object.getTermLength());
        }
    };

    @Override
    public void add(String bi, BigrammUnit unit) {
        storedMap.put(bi, unit);
    }

    @Override
    public BigrammUnit[] get(String bi) throws Exception {
        ArrayList<BigrammUnit> result = new ArrayList<>();
        try (CloseableIterator<BigrammUnit> it = new BerkeleyCloseableStoredIterator<>(((StoredValueSet<BigrammUnit>)storedMap.duplicates(bi)).storedIterator())) {
            while (it.hasNext())
                result.add(it.next());
        }
        return result.toArray(new BigrammUnit[result.size()]);
    }

    @Override
    public void close() throws Exception {
        bigrammDB.close();
    }
}
