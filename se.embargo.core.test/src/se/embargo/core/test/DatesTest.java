package se.embargo.core.test;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

import se.embargo.core.Dates;

public class DatesTest extends TestCase {
	@Test
	public void testFormatRelativeTimeSpan() {
		String actual = Dates.formatRelativeTimeSpan(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES));
		assertEquals("2 minutes ago", actual);
	}
}
