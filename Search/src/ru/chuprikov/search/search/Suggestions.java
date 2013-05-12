package ru.chuprikov.search.search;

import ru.kirillova.search.normspellcorr.Suggestion;

import java.util.ArrayList;
import java.util.List;

class Suggestions {

    private final ArrayList<List<Suggestion>> suggestions = new ArrayList<>();

    String[] getSuggestions(int maxSuggestions) {
        ArrayList<String> result = new ArrayList<>();
        int[] current = new int[suggestions.size()];

        while (true && result.size() < maxSuggestions) {
            StringBuilder newSuggestion = new StringBuilder();
            for (int i = 0; i < current.length; i++) {
                newSuggestion.append(suggestions.get(i).get(current[i]).getWord());
            }
            result.add(newSuggestion.toString());

            int minSuggestion = -1;
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < current.length; i++)
                if (current[i] < suggestions.get(i).size() - 1 && suggestions.get(i).get(current[i] + 1).getDistance() < minDistance) {
                    minSuggestion = i;
                    minDistance = suggestions.get(i).get(current[i] + 1).getDistance();
                }
            if (minDistance > 0.5 * Double.MAX_VALUE)
                break;
            else
                current[minSuggestion]++;
        }

        return result.toArray(new String[result.size()]);
    }

    void appendSuggestions(List<Suggestion> s) {
        suggestions.add(s);
    }

    void appendToken(String token) {
        suggestions.add(new ArrayList<Suggestion>());
        suggestions.get(suggestions.size() - 1).add(new Suggestion(token, 0));
    }
}
