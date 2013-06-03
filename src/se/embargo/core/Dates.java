package se.embargo.core;

import android.text.format.DateUtils;

public class Dates {
	/**
	 * Formats a date to a user presentable string.
	 * @param	ts	Timestamp to format in milliseconds since the Unix Epoch.
	 * @return		A string containing a formatted date.
	 */
	public static String formatRelativeTimeSpan(long ts) {
		return DateUtils.getRelativeTimeSpanString(ts).toString();
	}
}
