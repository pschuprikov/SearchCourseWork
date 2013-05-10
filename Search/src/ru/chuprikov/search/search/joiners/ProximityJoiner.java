package ru.chuprikov.search.search.joiners;

import ru.chuprikov.search.search.PositionInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class ProximityJoiner implements Joiner {
    private final int proximity;

    ProximityJoiner(int proximity) {
        this.proximity = proximity;
    }

    @Override
    public List<PositionInfo> join(List<PositionInfo> fst, List<PositionInfo> snd) {
        HashSet<PositionInfo> merged = new HashSet<>();
        for (PositionInfo f : fst)
            for (PositionInfo s : snd)
            {
                if (Math.min(f.getToIdx(), s.getToIdx()) - Math.max(f.getFromIdx(), s.getFromIdx()) >= -proximity)
                    merged.add(new PositionInfo(Math.min(f.getFromIdx(), s.getFromIdx()), Math.max(f.getToIdx(), s.getToIdx())));
            }
        return new ArrayList<>(merged);
    }
}
