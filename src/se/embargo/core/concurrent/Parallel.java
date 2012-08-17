package se.embargo.core.concurrent;

import java.io.File;
import java.io.FileFilter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import android.util.Log;

public class Parallel {
	private static final String TAG = "Parallel";
	private static int _corecount = -1;
	private static ExecutorService _threadpool = Executors.newFixedThreadPool(getNumberOfCores());
	
	/**
	 * Executes a functor in parallel over an input defined by the half open range [start, last)
	 * @param item		Work item to process
	 * @param start		Start of range
	 * @param last		End of range
	 * @param grainsize	Number of items for process in each chunk
	 */
	public static <Item> void forRange(ForBody<Item> body, Item item, int start, int last, int grainsize) {
		if (getNumberOfCores() > 1 && start + 1 < last) {
			int count = last - start;
			int slots = count % grainsize == 0 ? count / grainsize : count / grainsize + 1;
			CountDownLatch latch = new CountDownLatch(slots);
			
			// Invoke body in parallel over chunks
			for (int it = start; it < last; it += grainsize) {
				_threadpool.execute(new ForAdapter<Item>(latch, body, item, it, Math.min(it + grainsize, last)));
			}
			
			// Wait for tasks to complete
			for (;;) {
				try {
					latch.await();
					break;
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			body.run(item, start, last);
		}
	}
	
	/**
	 * Executes a functor in parallel over an input defined by the half open range [start, last)
	 * @param item		Work item to process
	 * @param start		Start of range
	 * @param last		End of range
	 */
	public static <Item> void forRange(ForBody<Item> body, Item item, int start, int last) {
		int count = last - start;
		int grainsize = Math.max(count / (getNumberOfCores() * 4), 1);
		forRange(body, item, start, last, grainsize);
	}
	
	/**
	 * Executes a functor in parallel over an input defined by the half open range [start, last)
	 * @param item		Work item to process
	 * @param start		Start of range
	 * @param last		End of range
	 */
	public static <Item, Result> Result mapReduce(MapReduceBody<Item, Result> body, Item item, int start, int last) {
		if (getNumberOfCores() > 1 && start + 1 < last) {
			int count = last - start;
			int grainsize = Math.max(count / (getNumberOfCores() * 4), 1);
			int slots = count % grainsize == 0 ? count / grainsize : count / grainsize + 1;
			CountDownLatch latch = new CountDownLatch(slots);
			Queue<Result> results = new ConcurrentLinkedQueue<Result>();
			
			// Invoke body in parallel over chunks
			for (int it = start; it < last; it += grainsize) {
				_threadpool.execute(new MapReduceAdapter<Item, Result>(latch, results, body, item, it, Math.min(it + grainsize, last)));
			}
			
			// Wait for tasks to complete
			for (;;) {
				try {
					latch.await();
					break;
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Merge remaining results
			Result lhs = results.poll();
			for (Result rhs = results.poll(); rhs != null; rhs = results.poll()) {
				lhs = body.reduce(lhs, rhs);
			}
			
			return lhs;
		}

		return body.map(item, start, last);
	}

	/**
	 * Gets the number of cores available in this device, across all processors.
	 * @remark	Requires access to the filesystem at "/sys/devices/system/cpu"
	 * @return	The number of cores, or 1 if failed to get result
	 */
	public static int getNumberOfCores() {
		if (_corecount < 0) {
		    try {
		        File dir = new File("/sys/devices/system/cpu/");
		        File[] files = dir.listFiles(new CpuFilter());
		        _corecount = files.length;
		    }
		    catch (Exception e) {
		    	Log.w(TAG, "Failed to detect number of CPU cores", e);
		    }

		    Log.i(TAG, "Number of CPU cores: " + _corecount);
	    	_corecount = Math.max(_corecount, 1);
		}
		
		return _corecount;
	}

	/**
	 * Adapts a ForBody instance to the Runnable interface
	 */
	private static class ForAdapter<Item> implements Runnable {
		private final CountDownLatch _latch;
		private final ForBody<Item> _body;
		private final Item _item;
		private final int _it, _last;

		public ForAdapter(CountDownLatch latch, ForBody<Item> body, Item item, int it, int last) {
			_latch = latch;
			_body = body;
			_item = item;
			_it = it;
			_last = last;
		}
		
		@Override
		public void run() {
			try {
				_body.run(_item, _it, _last);
			}
			finally {
				_latch.countDown();
			}
		}
	}
	
	/**
	 * Adapts a ForBody instance to the Runnable interface
	 */
	private static class MapReduceAdapter<Item, Result> implements Runnable {
		private final CountDownLatch _latch;
		private final Queue<Result> _results;
		private final MapReduceBody<Item, Result> _body;
		private final Item _item;
		private final int _it, _last;

		public MapReduceAdapter(CountDownLatch latch, Queue<Result> results, MapReduceBody<Item, Result> body, Item item, int it, int last) {
			_latch = latch;
			_results = results;
			_body = body;
			_item = item;
			_it = it;
			_last = last;
		}
		
		@Override
		public void run() {
			try {
				// Create partial result
				Result lhs = _body.map(_item, _it, _last);
				
				// Merge with other partial results
				for (Result rhs = _results.poll(); rhs != null; rhs = _results.poll()) {
					lhs = _body.reduce(lhs, rhs);
				}
				
				// Add merged result to output
				_results.add(lhs);
			}
			finally {
				_latch.countDown();
			}
		}
	}
	
    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }      
    }
}
