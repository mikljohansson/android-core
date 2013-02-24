package se.embargo.core.database;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utilities for working with loaders.
 */
public class Loaders {
	private static final AtomicInteger _idseq = new AtomicInteger(0);
	
	/**
	 * Each loader needs a unique sequence number.
	 * @return	A unique sequence number.
	 */
	public static int createSequenceNumber() {
		return _idseq.incrementAndGet();
	}
}
