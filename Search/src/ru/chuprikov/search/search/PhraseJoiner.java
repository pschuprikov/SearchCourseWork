package ru.chuprikov.search.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class PhraseJoiner implements Joiner {

    @Override
    public List<PositionInfo> join(List<PositionInfo> fst, List<PositionInfo> snd) {
        HashSet<PositionInfo> merged = new HashSet<>();
        for (PositionInfo f : fst)
            for (PositionInfo s : snd)
                if (f.getToIdx() + 1 == s.getFromIdx())
                    merged.add(new PositionInfo(f.getFromIdx(), s.getToIdx()));
        return new ArrayList<>(merged);
    }
}
