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
	public static final int FLAG_ENLARGE = 0x01;
	
	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param width		Max height of returned image
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
	 * Transform output info
	 */
	public static class Transform {
		public final Matrix matrix;
		public final int width;
		public final int height;
		
		public Transform(Matrix matrix, int width, int height) {
			this.matrix = matrix;
			this.width = width;
			this.height = height;
		}
	}
	
	/**
	 * Resizes a bitmap so that it's constrained by the given dimensions
	 * @param 	bm		Bitmap to resize
	 * @param 	width	Max height of returned image
	 * @param 	height	Max width of returned image
	 * @param 	flags	Flags controlling the process
	 * @param	rotate	Number of degrees to rotate the image
	 * @param	flip	Flip the image vertically
	 */
	public static Transform createTransform(int inputwidth, int inputheight, int maxwidth, int maxheight, int flags, int rotate, boolean mirror) {
		// Select the constraining dimension
		int targetwidth = inputwidth, targetheight = inputheight;
		if (targetwidth > maxwidth || (flags & FLAG_ENLARGE) != 0 && targetwidth != maxwidth) {
			targetheight = (int)((float)maxwidth / targetwidth * targetheight);
			targetwidth = maxwidth;
		}
		
		if (targetheight > maxheight) {
			targetwidth = (int)((float)maxheight / targetheight * targetwidth);
			targetheight = maxheight;
		}

		RectF inputcoord = new RectF(0, 0, inputwidth, inputheight);
		RectF outputcoord;
		
		// Flip the image
		float sx = 1.0f, sy = 1.0f;
		if (mirror) {
			sy *= -1.0;
			
			// Adjust the input coordinates to sample the flipped image
			inputcoord.top -= inputheight;
			inputcoord.bottom -= inputheight;
		}

		// Adjust the coordinates for the rotation
		switch (rotate) {
			case 90:
				outputcoord = new RectF(targetheight, 0, targetwidth + targetheight, targetheight);
				break;
				
			case 180:
				outputcoord = new RectF(targetwidth, targetheight, targetwidth * 2, targetheight * 2);
				break;
				
			case 270:
				outputcoord = new RectF(0, targetwidth, targetwidth, targetheight + targetwidth);
				break;
			
			default:
				outputcoord = new RectF(0, 0, targetwidth, targetheight);
				break;
		}
		
		// Create the transform matrix used for drawing
		Matrix matrix = new Matrix();
		matrix.setRectToRect(inputcoord, outputcoord, Matrix.ScaleToFit.START);
		matrix.preScale(sx, sy);
		matrix.postRotate(rotate, outputcoord.left, outputcoord.top);

		// Switch dimensions
		if (rotate == 90 || rotate == 270) {
			int tmpwidth = targetwidth;
			targetwidth = targetheight;
			targetheight = tmpwidth;
		}

		return new Transform(matrix, targetwidth, targetheight);
	}

	/**
	 * Resizes a bitmap so that it's constrained by the given dimensions
	 * @param 	bm			Bitmap to transform
	 * @param	transform	Transformation to apply
	 * @return				The transformed bitmap
	 */
	public static Bitmap transform(Bitmap bm, Transform transform) {
		Bitmap output = Bitmap.createBitmap(transform.width, transform.height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawBitmap(bm, transform.matrix, null);
		return output;
	}

	/**
	 * Resizes a bitmap so that it's constrained by the given dimensions
	 * @param 	bm		Bitmap to resize
	 * @param 	width	Max height of returned image
	 * @param 	height	Max width of returned image
	 * @return			The resized bitmap
	 */
	public static Bitmap resize(Bitmap bm, int width, int height) {
		return transform(bm, createTransform(bm.getWidth(), bm.getHeight(), width, height, 0, 0, false));
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
