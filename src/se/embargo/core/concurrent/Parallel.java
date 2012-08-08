package se.embargo.core.concurrent;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Parallel {
	private static final int _corecount = getNumberOfCores();
	
	private static final ExecutorService _threadpool = Executors.newFixedThreadPool(getNumberOfCores());
	
	/**
	 * Executes 
	 * @param item
	 * @param start
	 * @param last
	 */
	public static <T> void forRange(IForBody<T> body, T item, int start, int last) {
		if (false && _corecount > 1 && start + 1 < last) {
			int count = last - start;
			int grainsize = Math.max(count / (_corecount * 4), 1);
			int slots = count % grainsize == 0 ? count / grainsize : count / grainsize + 1;
			CountDownLatch latch = new CountDownLatch(slots);
			
			for (int it = start; it < last; it += grainsize) {
				_threadpool.execute(new ForBodyAdapter<T>(latch, body, item, it, Math.min(it + grainsize, last)));
			}
			
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
	 * Gets the number of cores available in this device, across all processors.
	 * @remark	Requires access to the filesystem at "/sys/devices/system/cpu"
	 * @return	The number of cores, or 1 if failed to get result
	 */
	public static int getNumberOfCores() {
	    try {
	        File dir = new File("/sys/devices/system/cpu/");
	        File[] files = dir.listFiles(new CpuFilter());
	        return files.length;
	    }
	    catch (Exception e) {
	        return 1;
	    }
	}

	private static class ForBodyAdapter<T> implements Runnable {
		private final CountDownLatch _latch;
		private final IForBody<T> _body;
		private final T _item;
		private final int _it, _last;

		public ForBodyAdapter(CountDownLatch latch, IForBody<T> body, T item, int it, int last) {
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
	
    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }      
    }
}
