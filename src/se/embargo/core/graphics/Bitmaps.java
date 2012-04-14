package se.embargo.core.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class Bitmaps {
	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param maxWidth	Max height of returned image
	 * @param maxHeight	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeStream(File file, int maxWidth, int maxHeight, Bitmap.Config config) {
		InputStream is;

		// Check the size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = config;
        options.inJustDecodeBounds = true;

		try {
			is = new FileInputStream(file);
	        BitmapFactory.decodeStream(is, null, options);
		}
		catch (IOException e) {
			return null;
		}
        
        if (options.outWidth < 0 || options.outHeight < 0) {
        	return null;
        }
        
        // Figure if sub-sampling should be used
        options.inJustDecodeBounds = false;

        if (options.outWidth > maxWidth) {
        	double ratio = (double)options.outWidth / (double)maxWidth;
        	int samples = (int)Math.ceil(ratio);
        	options.inSampleSize = Math.max(samples, options.inSampleSize);
        }

        if (options.outHeight > maxHeight) {
        	double ratio = (double)options.outHeight / (double)maxHeight;
        	int samples = (int)Math.ceil(ratio);
        	options.inSampleSize = Math.max(samples, options.inSampleSize);
        }
        
        // Reset the stream and decode the image
		try {
			is = new FileInputStream(file);
	        return BitmapFactory.decodeStream(is, null, options);
		}
		catch (IOException e) {
			return null;
		}
	}

	public static Bitmap decodeStream(File file, int maxWidth, int maxHeight) {
		return decodeStream(file, maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
	}
}
