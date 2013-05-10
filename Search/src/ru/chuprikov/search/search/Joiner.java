package ru.chuprikov.search.search;

import java.util.List;

public interface Joiner {
    List<PositionInfo> join(List<PositionInfo> fst, List<PositionInfo> snd);
}
