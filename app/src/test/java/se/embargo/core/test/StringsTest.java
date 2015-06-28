package se.embargo.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import se.embargo.core.Strings;

public class StringsTest {
	@Test
	public void testUpperCaseWords() {
		assertEquals("Abc", Strings.upperCaseWords("abc"));
		assertEquals("123", Strings.upperCaseWords("123"));
		assertEquals("Abc 123 Def", Strings.upperCaseWords("abc 123 def"));
		assertEquals("AbC 123 DEf", Strings.upperCaseWords("abC 123 dEf"));
	}
}
