package ru.kirillova.search.normspellcorr;

public class Suggestion implements Comparable<Suggestion> {
    private final double distance;
    private final String word;

    public Suggestion(String word, double distance) {
        this.distance = distance;
        this.word = word;
    }

    public double getDistance() {
        return distance;
    }

    public String getWord() {
        return word;
    }

    @Override
    public int compareTo(Suggestion o) {
        if (distance != o.distance)
            return Double.compare(distance, o.distance);
        else
            return word.compareTo(o.getWord());
    }
}
