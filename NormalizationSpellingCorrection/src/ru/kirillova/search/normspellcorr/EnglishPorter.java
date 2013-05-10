package ru.kirillova.search.normspellcorr;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EnglishPorter implements Stemming {

	private final Set<Character> vowels = new HashSet<>();
    private final Set<Character> doubles = new HashSet<>();

	private String step0(String s) {
		Pattern r = Pattern.compile("(.*)('|'s|'s')$");
		Matcher m = r.matcher(s);
		if (m.matches()) {
			return m.replaceAll("$1");
		}

		return s;
	}

	private boolean shortSyllable(String s) {
		int i = s.length() - 1;
		while (i >= 0) {
			if (vowels.contains(s.charAt(i))) {
				break;
			}
			--i;
		}
		if (i == 0) {
			if (s.length() > 1) {
				if (vowels.contains(s.charAt(1))) {
					return false;
				}
			}
		} else {
			if (s.length() == i + 1) {
				return false;
			}
			if (vowels.contains(s.charAt(i - 1))
					|| vowels.contains(s.charAt(i + 1))
					|| (s.charAt(i + 1) == 'w')
					|| (s.charAt(i + 1) == 'x')
					|| (s.charAt(i + 1) == 'Y')) {
				return false;
			}
		}
		return true;
	}
	
	private String step1a(String s) {
		Pattern r = Pattern.compile("(.*)sses$");
		Matcher m = r.matcher(s);
		if (m.matches()) {
			return m.replaceAll("$1ss");
		}
		r = Pattern.compile("(.*)(ied+|ies*)$");
		m = r.matcher(s);
		if (m.matches()) {
			String term = m.replaceAll("$1"); 
			if (term.length() > 1) {
				return m.replaceAll("$1i");
			}
			return m.replaceAll("$1ie");
		}
		r = Pattern.compile("(.*)(us+|ss)$");
		m = r.matcher(s);
		if (m.matches()) {
			return s;
		}
		r = Pattern.compile("(.*)s$");
		m = r.matcher(s);
		if (m.matches()) {
			String term = m.replaceAll("$1");
			for (int i = 0; i < term.length() - 1; ++i) {
				if (vowels.contains(s.charAt(i))) {
					return m.replaceAll("$1");
				}
			}
		}
		return s;
	}

	private String step1b(String s) {
		Pattern r = Pattern.compile("(.*)(eed|eedly+)$");
		Matcher m = r.matcher(s);
		if (m.matches()) {
			String term = m.replaceAll("$1");
			if (term.length() >= findR1(s)) {
				return m.replaceAll("$1ee");
			}
			return s;
		}
		r = Pattern.compile("(.*)(ed|edly+|ing|ingly+)$");
		m = r.matcher(s);
		if (m.matches()) {
			boolean flag = false;
			String s1 = m.replaceAll("$1");
			for (int i = 0; i < s1.length(); ++i) {
				if (vowels.contains(s.charAt(i))) {
					flag = true;
					break;
				}
			}
			if (!flag)
				return s;
			r = Pattern.compile("(.*)(at|bl|iz)$");
			m = r.matcher(s1);
			if (m.matches()) {
				return m.replaceAll("$1$2e");
			}
			if ((s1.length() > 2)
					&& (s1.charAt(s1.length() - 1) == s1.charAt(s1.length() - 2))
					&& (doubles.contains(s1.charAt(s1.length() - 1)))) {
				return s1.substring(0, s1.length() - 1);
			}
			if (findR1(s1) != s1.length()) {
				return s1;
			}
			if (shortSyllable(s1)) {
				return (s1 + "e");
			} else {
				return s1;
			}
		}
		return s;
	}

	private String step1c(String s) {
		if ((s.length() > 1)
				&& ((s.charAt(s.length() - 1) == 'y') || (s
						.charAt(s.length() - 1) == 'Y'))
				&& (!vowels.contains(s.charAt(s.length() - 2)))) {
			StringBuilder result = new StringBuilder(s);
			result.setCharAt(result.length() - 1, 'i');
			return result.toString();
		}
		return s;
	}

	private String step2(String s) {
		int r1 = findR1(s.toString());
		Pattern r = Pattern.compile("(.*)(ational|ation|ator)$");
		Matcher m = r.matcher(s);
		int start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ate");
		}
		r = Pattern.compile("(.*)tional$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1tion");
		}
		r = Pattern.compile("(.*)enci$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ence");
		}	
		r = Pattern.compile("(.*)anci$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ance");
		}
		r = Pattern.compile("(.*)abli$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1able");
		}
		r = Pattern.compile("(.*)entli$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ent");
		}
		r = Pattern.compile("(.*)(izer|ization)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ize");
		}
		r = Pattern.compile("(.*)(alism|aliti|alli)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1al");
		}
		r = Pattern.compile("(.*)fulness$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ful");
		}
		r = Pattern.compile("(.*)(ousli|ousness)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ous");
		}
		r = Pattern.compile("(.*)(iveness|iviti)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ive");
		}
		r = Pattern.compile("(.*)(biliti|bli+)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ble");
		}
		r = Pattern.compile("(.*)ogi+$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1) && (s.charAt(start - 1) == 'l')) {
			return m.replaceAll("$1og");
		}
		r = Pattern.compile("(.*)fulli+$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ful");
		}
		r = Pattern.compile("(.*)lessli+$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1less");
		}
		r = Pattern.compile("(.*)(c|d|e|g|h|k|n|m|r|t)li+$");
		m = r.matcher(s);
		start = (m.replaceAll("$1$2")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1$2");
		}
		return s;
	}
	
	private String step3(String s) {
		int r1 = findR1(s);
		int r2 = findR2(s, r1);
		Pattern r = Pattern.compile("(.*)ational+$");
		Matcher m = r.matcher(s);
		int start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ate");
		}
		r = Pattern.compile("(.*)tional+$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1tion");
		}
		r = Pattern.compile("(.*)alize$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1al");
		}
		r = Pattern.compile("(.*)(icate|iciti|ical)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1ic");
		}
		r = Pattern.compile("(.*)(ful|ness)$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r1)) {
			return m.replaceAll("$1");
		}
		r = Pattern.compile("(.*)ative*$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r2)) {
			return m.replaceAll("$1");
		}
		return s;
	}
	
	private String step4(String s) {
		int r1 = findR1(s);
		int r2 = findR2(s, r1);
		Pattern r = Pattern.compile("(.*)(al|ance|ence|er|ic|able|ible|ant|ement|ism|ate|iti|ous|ive|ize)$");
		Matcher m = r.matcher(s);
		int start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r2)) {
			return m.replaceAll("$1");
		}
		r = Pattern.compile("(.*)ment$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r2)) {
			return m.replaceAll("$1");
		}
		r = Pattern.compile("(.*)ent$");
		m = r.matcher(s);
		start = (m.replaceAll("$1")).length();
		if (m.matches() && (start >= r2)) {
			return m.replaceAll("$1");
		}
		r = Pattern.compile("(.*)(s|t)ion$");
		m = r.matcher(s);
		start = (m.replaceAll("$1$2")).length();
		if (m.matches() && (start >= r2)) {
			return m.replaceAll("$1$2");
		}
		return s;
	}
	
	private String step5(String s) {
		int r1 = findR1(s);
		int r2 = findR2(s, r1);
		Pattern r = Pattern.compile("(.*)e$");
		Matcher m = r.matcher(s);
		if (m.matches()) {
			String term = m.replaceAll("$1");
			if (term.length() >= r2) 
				return term;
			if (term.length() < r1) {
				return s;
			}
			if(!shortSyllable(term)) {
				return term;
			}
			return s;
		}
		r = Pattern.compile("(.*l)l$");
		m = r.matcher(s);
		if (m.matches() && (r2 < s.length())) {
			return m.replaceAll("$1");
		}
		return s;
	}
	
	private int findR1(String s) {
		int r = s.length();
		for (int i = 1; i < s.length(); ++i) {
			if (vowels.contains(s.charAt(i - 1))
					&& (!vowels.contains(s.charAt(i)))) {
				r = i + 1;
				break;
			}
		}
		return r;
	}
	
	private int findR2(String s, int r1) {
		int r = s.length();
		for (int i = r1; i < s.length(); ++i) {
			if (vowels.contains(s.charAt(i - 1))
					&& (!vowels.contains(s.charAt(i)))) {
				r = i + 1;
				break;
			}
		}
		return r;
	}

	public String getBasis(String s) {
		StringBuilder result = new StringBuilder(s);
		if (s.length() <= 2) {
			s.replace('Y', 'y');
			return s;
		}
		vowels.add('a');
		vowels.add('e');
		vowels.add('i');
		vowels.add('o');
		vowels.add('u');
		vowels.add('y');
		doubles.add('b');
		doubles.add('d');
		doubles.add('f');
		doubles.add('g');
		doubles.add('m');
		doubles.add('n');
		doubles.add('p');
		doubles.add('r');
		doubles.add('t');
		if (result.charAt(0) == 'y') {
			result.setCharAt(0, 'Y');
		}
		for (int i = 1; i < result.length(); ++i) {
			if (vowels.contains(result.charAt(i - 1))
					&& (result.charAt(i) == 'y')) {
				result.setCharAt(i, 'Y');
			}
		}
		String s1 = step0(result.toString());
		if (s1.length() <= 2) return s1;
		s1 = step1a(s1);
		if (s1.length() <= 2) return s1;
		s1 = step1b(s1);
		if (s1.length() <= 2) return s1;
		s1 = step1c(s1);
		if (s1.length() <= 2) return s1;
		s1 = step2(s1);
		if (s1.length() <= 2) return s1;
		s1 = step3(s1);
		if (s1.length() <= 2) return s1;
		s1 = step4(s1);
		if (s1.length() <= 2) return s1;
		s1 = step5(s1);
		s1 = s1.replace('Y', 'y');
		return s1;
	}
}
