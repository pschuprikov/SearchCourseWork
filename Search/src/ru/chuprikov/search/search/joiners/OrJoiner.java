package ru.chuprikov.search.search.joiners;

import ru.chuprikov.search.search.PositionInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class OrJoiner implements Joiner {
    @Override
    public List<PositionInfo> join(List<PositionInfo> fst, List<PositionInfo> snd) {
        HashSet<PositionInfo> merged = new HashSet<>();
        merged.addAll(fst);
        merged.addAll(snd);
        return new ArrayList<>(merged);
    }
}
