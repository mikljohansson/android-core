package se.embargo.core;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class SystemInfo {
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

    /**
     * Check if filename is "cpu", followed by a single digit number
     */
    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }      
    }
}
