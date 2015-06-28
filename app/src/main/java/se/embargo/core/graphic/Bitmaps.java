package se.embargo.core.graphic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

/**
 * Utilities for working with bitmaps.
 */
public abstract class Bitmaps {
	/**
	 * Indicates that a bitmap should be enlarged to fit the target width or height.
	 */
	public static final int FLAG_ENLARGE = 0x01;
	
	/**
	 * Paint object used to draw on a surface using bitmap filtering.
	 */
	private static final Paint FILTER_BITMAP_PAINT = new Paint(Paint.FILTER_BITMAP_FLAG);
	
	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param width		Max height of returned image
	 * @param height	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeStream(InputStreamProvider provider, int width, int height, Bitmap.Config config) {
		InputStream is;

		// Check the size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = config;
        options.inJustDecodeBounds = true;

		try {
			is = provider.getInputStream();
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
			is = provider.getInputStream();
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
	 * @param resolver	Provides the input stream for the given uri
	 * @param uri		content:// uri of image to open
	 * @param width		Max height of returned image
	 * @param height	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeUri(ContentResolver resolver, Uri uri, int width, int height) {
		return decodeStream(new ContentStreamProvider(resolver, uri), width, height, Bitmap.Config.ARGB_8888);
	}
	
	/**
	 * Decodes and sub-samples an image
	 * @param is		Stream to read image from
	 * @param width		Max height of returned image
	 * @param height	Max width of returned image
	 * @param config	Bitmap target coding
	 * @return			An image constrained by the given bounds, or null on failure
	 */
	public static Bitmap decodeStream(File file, int width, int height, Bitmap.Config config) {
		return decodeStream(new FileStreamProvider(file), width, height, config);
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
		private final float[] _values;
		
		public Transform(Matrix matrix, int width, int height) {
			this.matrix = matrix;
			this.width = width;
			this.height = height;
			
			_values = new float[9];
			matrix.getValues(_values);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + width;
			result = prime * result + height;
			result = prime * result + Arrays.hashCode(_values);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			
			Transform other = (Transform)obj;
			if (width != other.width || height != other.height || !Arrays.equals(_values, other._values)) {
				return false;
			}
			
			return true;
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
		if (targetwidth > maxwidth || (flags & FLAG_ENLARGE) == FLAG_ENLARGE && (targetwidth != maxwidth || targetheight != maxheight)) {
			targetheight = (int)((float)maxwidth / targetwidth * targetheight);
			targetwidth = maxwidth;
		}
		
		if (targetheight > maxheight || (flags & FLAG_ENLARGE) == FLAG_ENLARGE && (targetwidth != maxwidth || targetheight != maxheight)) {
			targetwidth = (int)((float)maxheight / targetheight * targetwidth);
			targetheight = maxheight;
		}

		RectF inputcoord = new RectF(0, 0, inputwidth, inputheight);
		RectF outputcoord;
		
		// Flip the image
		float sx = 1.0f, sy = 1.0f;
		if (mirror) {
			if (rotate == 90 || rotate == 270) {
				sy *= -1.0;
				
				// Adjust the input coordinates to sample the flipped image
				inputcoord.top -= inputheight;
				inputcoord.bottom -= inputheight;
			}
			else {
				sx *= -1.0;
				
				// Adjust the input coordinates to sample the flipped image
				inputcoord.left -= inputwidth;
				inputcoord.right -= inputwidth;
			}
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
	 * Applies a transform to an input bitmap.
	 * @param 	bm			Bitmap to transform
	 * @param	transform	Transformation to apply
	 * @param	config		Output bitmap format
	 * @return				The transformed bitmap
	 */
	public static Bitmap transform(Bitmap bm, Transform transform, Bitmap.Config config) {
		Bitmap output = Bitmap.createBitmap(transform.width, transform.height, config);
		Canvas canvas = new Canvas(output);
		canvas.drawBitmap(bm, transform.matrix, FILTER_BITMAP_PAINT);
		return output;
	}

	/**
	 * Resizes a bitmap so that it's constrained by the given dimensions
	 * @param 	bm			Bitmap to transform
	 * @param	transform	Transformation to apply
	 * @return				The transformed bitmap
	 */
	public static Bitmap transform(Bitmap bm, Transform transform) {
		return transform(bm, transform, Bitmap.Config.ARGB_8888);
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
	
	/**
	 * Resizes a bitmap so that it's clipped by the given dimensions
	 * @param 	bm		Bitmap to resize
	 * @param 	width	Max height of returned image
	 * @param 	height	Max width of returned image
	 * @return			The resized bitmap
	 */
	public static Bitmap fit(Bitmap bm, int width, int height) {
		float inputratio = (float)bm.getWidth() / bm.getHeight(), 
			  outputratio = (float)width / height;
		
		RectF inputcoord;
		RectF outputcoord = new RectF(0, 0, width, height);

		if (inputratio >= outputratio) {
			int inputwidth = (int)((float)bm.getHeight() * outputratio);
			int offset = (bm.getWidth() - inputwidth) / 2;
			inputcoord = new RectF(offset, 0, inputwidth + offset, bm.getHeight());
		}
		else {
			int inputheight = (int)((float)bm.getWidth() * outputratio);
			int offset = (bm.getHeight() - inputheight) / 2;
			inputcoord = new RectF(0, offset, bm.getWidth(), inputheight + offset);
		}
		
		Matrix matrix = new Matrix();
		matrix.setRectToRect(inputcoord, outputcoord, Matrix.ScaleToFit.START);
		
		return transform(bm, new Transform(matrix, width, height));
	}

	/**
     * Rounds the corners of a bitmap
     * @param bitmap	Input bitmap
     * @param pixels	Radius of rounded corner
     * @return			A copy of the bitmap with the corners rounded
     */
    public static Bitmap getRoundedCorners(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * Rounds the corners of a bitmap
     * @param bitmap	Input bitmap
     * @return			A copy of the bitmap with the corners rounded
     */
    public static Bitmap getRoundedCorners(Bitmap bitmap) {
    	return getRoundedCorners(bitmap, 5);
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
	
	private static interface InputStreamProvider {
		public InputStream getInputStream() throws IOException;
	}
	
	private static class FileStreamProvider implements InputStreamProvider {
		private File _file;
		
		public FileStreamProvider(File file) {
			_file = file;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(_file);
		}
	}

	private static class ContentStreamProvider implements InputStreamProvider {
		private ContentResolver _resolver;
		private Uri _uri;
		
		public ContentStreamProvider(ContentResolver resolver, Uri uri) {
			_resolver = resolver;
			_uri = uri;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return _resolver.openInputStream(_uri);
		}
	}
}
