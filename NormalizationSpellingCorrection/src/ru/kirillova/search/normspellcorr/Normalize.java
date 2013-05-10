package ru.kirillova.search.normspellcorr;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Normalize {

    public static String getBasisWord(String s) {
        Stemming p;
        if ((s.charAt(0) >= 'a') && (s.charAt(0) <= 'z')) {
            p = new EnglishPorter();
        } else {
            p = new RussianPorter();
        }
        return p.getBasis(s);
    }

    public static List<String> getNormalTokens(String s) {
        List<String> result = new ArrayList <>();;
        Tokenization r = new Tokenization();
        List<String> l = r.getTokens(s);
        ListIterator<String> e = l.listIterator();
        while (e.hasNext()) {
            String str = e.next().toLowerCase();
            result.add(getBasisWord(str));
        }
        return result;
    }
}