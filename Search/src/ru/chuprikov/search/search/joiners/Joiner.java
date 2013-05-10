package ru.chuprikov.search.search.joiners;

import ru.chuprikov.search.search.PositionInfo;

import java.util.List;

public interface Joiner {
    List<PositionInfo> join(List<PositionInfo> fst, List<PositionInfo> snd);
}
