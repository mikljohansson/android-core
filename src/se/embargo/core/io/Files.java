package se.embargo.core.io;

import android.webkit.MimeTypeMap;

/**
 * Utilities for working with files.
 */
public class Files {
	/**
	 * Tries to detect the mimetype of a file.
	 * @param	path		Path to file.
	 * @param	mimetype	Tentative mimetype, may be null
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
	 * @param	path		Path to file.
	 */
	public static String getMimeType(String path) {
		return getMimeType(path, null);
	}
}
