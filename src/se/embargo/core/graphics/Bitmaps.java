package se.embargo.core.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

public abstract class Bitmaps {
	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param width	Max height of returned image
	 * @param height	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeStream(File file, int width, int height, Bitmap.Config config) {
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
        
        setSampleSize(width, height, options);
        
        // Reset the stream and decode the image
		Bitmap bm;
        try {
			is = new FileInputStream(file);
	        bm = BitmapFactory.decodeStream(is, null, options);
		}
		catch (IOException e) {
			return null;
		}
        
        // Resize the image to fit the bounds
        if (bm != null) {
        	return resize(bm, width, height);
        }
        
        return null;
	}

	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param width	Max height of returned image
	 * @param height	Max width of returned image
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeStream(File file, int width, int height) {
		return decodeStream(file, width, height, Bitmap.Config.ARGB_8888);
	}
	
	/**
	 * Decodes and sub-samples an image
	 * @param data		Image data
	 * @param width	Max height of returned image
	 * @param height	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeByteArray(byte[] data, int width, int height, Bitmap.Config config) {
		// Check the size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = config;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        
        if (options.outWidth < 0 || options.outHeight < 0) {
        	return null;
        }
        
        setSampleSize(width, height, options);
        
        // Decode the image
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        
        // Resize the image to fit the bounds
        if (bm != null) {
        	return resize(bm, width, height);
        }
        
        return null;
	}

	/**
	 * Decodes and sub-samples an image
	 * @param data		Image data
	 * @param width		Max height of returned image
	 * @param height	Max width of returned image
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeByteArray(byte[] data, int width, int height) {
		return decodeByteArray(data, width, height, Bitmap.Config.ARGB_8888);
	}

	/**
	 * Resizes a bitmap so that it's constrained by the given dimensions
	 * @param 	bm		Bitmap to resize
	 * @param 	width	Max height of returned image
	 * @param 	height	Max width of returned image
	 * @param	rotate	Number of degrees to rotate image
	 * @return	flip	Flip the image vertically
	 */
	public static Bitmap resize(Bitmap bm, int width, int height, float rotate, boolean flip) {
		// Select the constraining dimension
		int targetwidth = bm.getWidth(), targetheight = bm.getHeight();
		if (targetwidth > width) {
			targetheight = (int)((float)width / targetwidth * targetheight);
			targetwidth = width;
		}
		
		if (targetheight > height) {
			targetwidth = (int)((float)height / targetheight * targetwidth);
			targetheight = height;
		}
		
		// Don't enlarge the image
		if (targetwidth > bm.getWidth() || targetheight > bm.getHeight()) {
			return bm;
		}
		
		// Create input and output bitmaps
		Bitmap target = Bitmap.createBitmap(targetwidth, targetheight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		
		// Scale the image when drawing
		Matrix matrix = new Matrix();
		matrix.setRectToRect(
			new RectF(0, 0, bm.getWidth(), bm.getHeight()), 
			new RectF(0, 0, targetwidth, targetheight), 
			Matrix.ScaleToFit.START);

		if (flip) {
			matrix.preScale(-1, 1);
		}
		
		if (rotate != 0.0f) {
			matrix.postRotate(rotate);
		}
		
		canvas.drawBitmap(bm, matrix, null);
		return target;
	}
	
	public static Bitmap resize(Bitmap bm, int width, int height) {
		return resize(bm, width, height, 0.0f, false);
	}
	
	private static void setSampleSize(int width, int height, BitmapFactory.Options options) {
		// Figure if sub-sampling should be used
        options.inJustDecodeBounds = false;

        if (options.outWidth > width) {
        	double ratio = (double)options.outWidth / (double)width;
        	int samples = (int)Math.max(Math.floor(ratio), 1.0);
        	options.inSampleSize = options.inSampleSize > 1 ? Math.min(samples, options.inSampleSize) : samples;
        }

        if (options.outHeight > height) {
        	double ratio = (double)options.outHeight / (double)height;
        	int samples = (int)Math.max(Math.floor(ratio), 1.0);
        	options.inSampleSize = options.inSampleSize > 1 ? Math.min(samples, options.inSampleSize) : samples;
        }
	}
}
