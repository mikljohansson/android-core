package se.embargo.core.database;

import java.util.concurrent.atomic.AtomicInteger;

public class Loaders {
	private static final AtomicInteger _idseq = new AtomicInteger(0);
	
	public static int createSequenceNumber() {
		return _idseq.incrementAndGet();
	}
}
