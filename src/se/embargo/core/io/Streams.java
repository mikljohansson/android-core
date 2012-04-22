package se.embargo.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Streams {
	public static String toString(InputStream is) throws IOException {
		InputStreamReader input = new InputStreamReader(is/*, "UTF-8"*/);
		final int CHARS_PER_PAGE = 5000;
		final char[] buffer = new char[CHARS_PER_PAGE];
		StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
		
		for (int read = input.read(buffer, 0, buffer.length); read != -1;
				 read = input.read(buffer, 0, buffer.length)) {
			output.append(buffer, 0, read);
		}

		return output.toString();
	}
}
