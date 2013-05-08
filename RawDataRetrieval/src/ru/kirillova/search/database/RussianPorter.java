package ru.kirillova.search.database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RussianPorter implements Stemming {

    private String perfectiveGerund(String s) {
        Pattern r = Pattern.compile("(.*)(ив|ивши|ившись|ыв|ывши|ывшись)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        r = Pattern.compile("(.*(а|я))(в|вши|вшись)$");
        m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private String adjectival(String s) {
        Pattern r = Pattern
                .compile("(.*)(ее|ие|ые|ое|ими|ыми|ей|ий|ый|ой|ем|им|ым|ом|его|ого|ему|ому|их|ых|ую|юю|ою|ею|ая|яя)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            String s1 = m.replaceAll("$1");
            r = Pattern.compile("(.*)(ивш|ывш|ующ)$");
            m = r.matcher(s1);
            if (m.matches()) {
                return m.replaceAll("$1");
            }
            r = Pattern.compile("(.*(а|я))(ем|нн|вш|ющ|щ)$");
            m = r.matcher(s1);
            if (m.matches()) {
                return m.replaceAll("$1");
            }
        }
        return "";
    }

    private String reflexive(String s) {
        Pattern r = Pattern.compile("(.*)(ся|сь)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private String verb(String s) {
        Pattern r = Pattern
                .compile("(.*)(ила|ыла|ена|ейте|уйте|ите|или|ыли|ей|уй|ил|ыл|им|ым|ен|ило|ыло|ено|ят|ует|уют|ит|ыт|ены|ить|ыть|ишь|ую)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        r = Pattern
                .compile("(.*(а|я))(ла|на|ете|йте|ли|й|л|ем|н|ло|ет|ют|ны|ть|ешь|нно|но)$");
        m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        r = Pattern.compile("(.*)ю$");
        m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private String noun(String s) {
        Pattern r = Pattern
                .compile("(.*)(а|ев|ов|ие|ье|иями|ами|еи|ии|ией|ой|ий|иям|ием|ам|ом|о|у|ах|иях|ы|ь|ию|ью|ия|ья)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        r = Pattern.compile("(.*)(ей|ям|ем|ях|ями|ю|я|е)");
        m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        r = Pattern.compile("(.*)(и|й)");
        m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private String superlative(String s) {
        Pattern r = Pattern.compile("(.*)(ейш|ейше)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private String derevational(String s) {
        Pattern r = Pattern.compile("(.*)(ост|ость)$");
        Matcher m = r.matcher(s);
        if (m.matches()) {
            return m.replaceAll("$1");
        }
        return "";
    }

    private boolean vowel(char c) {
        if ((c == 'а') || (c == 'е') || (c == 'и') || (c == 'о') || (c == 'у')
                || (c == 'ы') || (c == 'э') || (c == 'ю') || (c == 'я')) {
            return true;
        }
        return false;
    }

    public String getBasis(String str) {
        if (str.length() <= 3)
            return str;
        String curs = str;
        String s1 = perfectiveGerund(curs);
        // first step
        if (s1.equals("")) {
            s1 = reflexive(curs);
            if (!s1.equals("")) {
                curs = s1;
                if (curs.length() <= 3)
                    return curs;
            }
            s1 = adjectival(curs);
            if (!s1.equals("")) {
                curs = s1;
                if (curs.length() <= 3)
                    return curs;
            } else {
                s1 = verb(curs);
                if (!s1.equals("")) {
                    curs = s1;
                    if (curs.length() <= 3)
                        return curs;
                } else {
                    s1 = noun(curs);
                    if (!s1.equals("")) {
                        curs = s1;
                        if (curs.length() <= 3)
                            return curs;
                    }
                }
            }

        } else {
            curs = s1;
            if (curs.length() <= 3)
                return curs;
        }
        // second step
        Pattern r = Pattern.compile("(.*)и$");
        Matcher m = r.matcher(curs);
        if (m.matches()) {
            curs = m.replaceAll("$1");
            if (curs.length() <= 3)
                return curs;
        }
        // third step
        int pos = 0;
        for (int t = 0; t < 2; ++t) {
            boolean flag = false;
            for (int i = pos; i < curs.length(); ++i) {
                if (vowel(curs.charAt(i))) {
                    flag = true;
                } else if (flag) {
                    pos = i;
                    break;
                }
            }
        }
        if (pos < curs.length()) {
            s1 = curs.substring(pos);
            s1 = derevational(s1);
            if (!s1.equals("")) {
                curs = s1;
                if (curs.length() <= 3)
                    return curs;
            }
        }
        // last step
        r = Pattern.compile("(.*)нн(.*)");
        m = r.matcher(curs);
        if (m.matches()) {
            curs = m.replaceAll("$1$2");
            if (curs.length() <= 3)
                return curs;
        }
        s1 = superlative(curs);
        if (!s1.equals("")) {
            curs = s1;
            if (curs.length() <= 3)
                return curs;
        }
        if (curs.charAt(curs.length() - 1) == 'ь') {
            curs = curs.substring(0, curs.length() - 1);
        }
        return curs;
    }
}
