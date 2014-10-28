package se.embargo.core.io;

import java.io.File;

import android.webkit.MimeTypeMap;

/**
 * Utilities for working with files.
 */
public class Files {
	/**
	 * Tries to detect the mimetype of a file.
	 * @param	path		Path to file.
	 * @param	mimetype	Tentative mimetype, may be null
	 * @return				Detected mimetype, defaults to application/octet-stream
	 */
	public static String getMimeType(String path, String mimetype) {
		// ES File Manager may return "*/*" as the mime type
		if (mimetype == null || "*/*".equals(mimetype)) {
			int pos = path.lastIndexOf('.');
			String extension = pos >= 0 ? path.substring(pos + 1) : "";
			mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		
		return mimetype;
	}
	
	/**
	 * Tries to detect the mimetype of a file.
	 * @param	path	Path to file.
	 * @return			Detected mimetype, defaults to application/octet-stream
	 */
	public static String getMimeType(String path) {
		return getMimeType(path, null);
	}
	
	/**
	 * Returns the N last parts of a directory and file path.
	 * @param path	Path to file.
	 * @param count	Number of trailing parts to return.
	 * @return		The trailing path segment.
	 */
	public static String getTrailingPath(String path, int count) {
		String[] parts = path.split(File.separator);
		String result = parts[parts.length - 1];
		for (int i = parts.length - 2; i >= Math.max(parts.length - count, 0); i--) {
			result = parts[i] + File.separator + result;
		}
		return result;
	}
}
