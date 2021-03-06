package ru.kirillova.search.normspellcorr;

import ru.chuprikov.search.database.BigrammDB;
import ru.chuprikov.search.database.TermDB;
import ru.chuprikov.search.datatypes.BigrammUnit;
import ru.chuprikov.search.datatypes.Term;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kgramm {
    private final BigrammDB bigrammDB;
    private final TermDB termDB;

    public Kgramm(TermDB termDB, BigrammDB bigrammDB) {
        this.bigrammDB = bigrammDB;
        this.termDB = termDB;
    }

    private List<Term> getSimilarTerms(String s) throws Exception {
		List<Term> result = new ArrayList<>();
		Map<BigrammUnit, Integer> words = new HashMap<>();
		for (int i = 0; i < s.length(); ++i) {
			BigrammUnit[] terms = bigrammDB.get("" + s.charAt(i % s.length()) + s.charAt((i + 1) % s.length()));
			for (BigrammUnit bu : terms) {
                Term term = termDB.get(bu.getTermID());
                if (Math.abs(bu.getTermLength() - s.length()) > 2) continue;
				if (words.containsKey(bu)) {
					words.put(bu, words.get(bu) + 1);
				} else {
					words.put(bu, 1);
				}
			}
		}
        for (Entry<BigrammUnit, Integer> e : words.entrySet()) {
			double t = ((double)e.getValue())
					/ (s.length() - 1 + e.getKey().getTermLength() - 1 - e.getValue());
			if (t >= 0.5) {
				result.add(termDB.get(e.getKey().getTermID()));
			}
        }
		return result;
	}

	private double costExchange(char a, char b) {
		if ((a >= 'a') && (a <= 'z')) {
			Pattern r[] = new Pattern[9];
			r[0] = Pattern.compile("(a|o|e|i|u|y)");
			r[1] = Pattern.compile("(b|p)");
			r[2] = Pattern.compile("(c|k|q)");
			r[3] = Pattern.compile("(d|t)");
			r[4] = Pattern.compile("(r|l)");
			r[5] = Pattern.compile("(m|n)");
			r[6] = Pattern.compile("(g|j)");
			r[7] = Pattern.compile("(f|v)");
			r[8] = Pattern.compile("(s|x|z)");
			Matcher m1;
			Matcher m2;
			for (int i = 0; i < 9; ++i) {
				m1 = r[i].matcher("" + a);
				m2 = r[i].matcher("" + b);
				if (m1.matches() && m2.matches()) {
					return 0.7;
				}
			}
		}
        if ((a >= 'а') && (a <= 'я')) {
            Pattern r[] = new Pattern[8];
            r[0] = Pattern.compile("(а|о|е|и|ё|я|ы|у|ю|э)");
            r[1] = Pattern.compile("(б|п)");
            r[2] = Pattern.compile("(з|с)");
            r[3] = Pattern.compile("(к|г)");
            r[4] = Pattern.compile("(ш|щ)");
            r[5] = Pattern.compile("(в|ф)");
            r[6] = Pattern.compile("(д|т)");
            r[7] = Pattern.compile("(л|м|н)");
            Matcher m1;
            Matcher m2;
            for (int i = 0; i < 8; ++i) {
                m1 = r[i].matcher("" + a);
                m2 = r[i].matcher("" + b);
                if (m1.matches() && m2.matches()) {
                    return 0.7;
                }
            }
        }
        return 1.5;
    }

	private double levDist(String s1, String s2) {
		double w_insert = 2.;
		double w_transp = 1.;
		int n = s1.length();
		int m = s2.length();
		if (n == 0) {
			if (m == 0) {
				return 0;
			}
			return m * w_insert;
		}
		if (m == 0) {
			return n * w_insert;
		}
		double d[][] = new double[n + 2][m + 2];
		double inf = w_insert * Math.max(n, m);
		d[0][0] = inf;
		for (int i = 0; i <= n; ++i) {
			d[i + 1][1] = i;
			d[i + 1][0] = inf;
		}
		for (int i = 0; i <= m; ++i) {
			d[1][i + 1] = i;
			d[0][i + 1] = inf;
		}
		Map<Character, Integer> lastPosition = new TreeMap<>();
		for (int i = 0; i < n; ++i) {
			int last = 0;
			for (int j = 0; j < m; ++j) {
				int i1;
				if (lastPosition.containsKey(s2.charAt(j))) {
					i1 = lastPosition.get(s2.charAt(j));
				} else {
					i1 = 0;
				}
				int j1 = last;
				if (s1.charAt(i) == s2.charAt(j)) {
					d[i + 2][j + 2] = d[i + 1][j + 1];
					last = j;
				} else {
					d[i + 2][j + 2] = Math.min(
							d[i + 1][j + 1]
									+ costExchange(s1.charAt(i), s2.charAt(j)),
							Math.min(d[i + 2][j + 1] + w_insert,
									d[i + 1][j + 2] + w_insert));
				}
				d[i + 2][j + 2] = Math.min(d[i + 2][j + 2], d[i1 + 1][j1 + 1]
						+ (i - i1) + (j - j1) + w_transp);
			}
			lastPosition.put(s1.charAt(i), i);
		}
		return d[n + 1][m + 1];
	}

    public List<Suggestion> fixMistake(String str) throws Exception {
        String s = Normalize.getBasisWord(str);
        String ending = str.substring(s.length(), str.length());
        List<Term> terms = getSimilarTerms(s);
        Map<String, Double> nearestWords = new HashMap<>();

        for (Term term : terms) {
            double d = levDist(s, term.getTerm());
            if (d <= 3.) {
                nearestWords.put(term.getTerm() + ending, d);
            }
        }

        // можно добавить функцию рейтинга)

        List<Suggestion> result = new ArrayList<>();
        for (Entry<String, Double> e : nearestWords.entrySet()) {
            result.add(new Suggestion(e.getKey(), e.getValue()));
        }
        Collections.sort(result);

        while (result.size() > 10) {
            result.remove(result.size() - 1);
        }

        return result;
    }
}
