package ru.kirillova.search.normspellcorr;

import java.util.ArrayList;
import java.util.List;


public class Tokenization {
	
	public static List <String> getTokens(String str) {
		List <String> result = new ArrayList <>();
		for(int i = 0; i < str.length(); ++i) {
			if (Character.isLetter(str.charAt(i))) {
				StringBuilder word = new StringBuilder();
				int count = 0;
				do {
					word.append(str.charAt(i));
					++count;
					i++;
					if ((i < str.length()) && (str.charAt(i) == '.')) {
						if (count == 1) {
							count = 0;
							++i;
						}
					}
				} while ((i < str.length() && Character.isLetter(str.charAt(i))));
				result.add(word.toString().toLowerCase());
			}
		}
		return result;
	}		
}
