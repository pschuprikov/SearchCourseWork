package ru.chuprikov.search.search;

public class PositionInfo {
    private final int fromIdx;

    public PositionInfo(int fromIdx, int toIdx) {
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
    }

    private final int toIdx;

    public int getFromIdx() {
        return fromIdx;
    }

    public int getToIdx() {
        return toIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionInfo that = (PositionInfo) o;

        if (fromIdx != that.fromIdx) return false;
        if (toIdx != that.toIdx) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromIdx;
        result = 31 * result + toIdx;
        return result;
    }
}
