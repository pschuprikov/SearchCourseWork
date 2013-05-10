package ru.chuprikov.search.search.joiners;

public class Joiners {
    private static final Joiner orJoiner = new OrJoiner();
    private static final Joiner phraseJoiner = new PhraseJoiner();

    private static final Joiner[] proximityJoiners = new Joiner[10];

    static {
        for (int i = 0; i < proximityJoiners.length; i++) {
            proximityJoiners[i] = new ProximityJoiner(i);
        }
    }

    public static Joiner getOrJoiner() {
        return orJoiner;
    }

    public static Joiner getProximityJoiner(int proximity) {
        return proximity < proximityJoiners.length ? proximityJoiners[proximity] : new ProximityJoiner(proximity);
    }

    public static Joiner getPhraseJoiner() {
        return phraseJoiner;
    }
}
