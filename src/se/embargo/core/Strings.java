package se.embargo.core;

/**
 * Utilities for working with strings.
 */
public class Strings {
	/**
	 * Parses an integer.
	 * @param	value	String to parse.
	 * @param	defval	Default value in case string fails to parse.
	 * @return			Parsed integer.
	 */
	public static int parseInt(String value, int defval) {
		try {
			return Integer.parseInt(value);
		}
		catch (Exception e) {}
		
		return defval;
	}
	
	/**
	 * Uppercase the first letter of every word.
	 * @param	value	String to uppercase.
	 * @return			Processed string.
	 */
	public static String upperCaseWords(String value) {
		final StringBuilder result = new StringBuilder(value.length());
		String[] words = value.split("\\s");
		
		for (int i = 0, l = words.length; i < l; ++i) {
			if (words[i].length() > 0) {
				if (i > 0) {
					result.append(" ");
				}
	
				result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
			}
		}
		
		return result.toString();
	}
}
