package se.embargo.core.graphic;

/**
 * Utilities for working with images.
 */
public class Levels {
	/**
	 * Calculates the global luminance threshold using Otsu's method 
	 * @param imagewidth	Width of input image
	 * @param imageheight	Height of input image
	 * @param image			Input image buffer
	 * @param histogram		Histogram of the input image, length must be 256 
	 * @return				The global threshold color
	 */
	public static int getThreshold(int imagewidth, int imageheight, final int[] image, final int[] histogram) {
		float sum = 0;
		int pixels = imagewidth * imageheight;
		
		for (int i = 0; i < histogram.length; i++) {
			sum += (float)(histogram[i] * i);
		}
		
		float csum = 0;
		int wB = 0;
		int wF = 0;
		
		float fmax = -1.0f;
		int threshold = 0;
		
		for (int i = 0; i < histogram.length; i++) {
			// Weight background
			wB += histogram[i];
			if (wB == 0) { 
				continue;
			}
		
			// Weight foreground
			wF = pixels - wB;
			if (wF == 0) {
				break;
			}
		
			csum += (float)(histogram[i] * i);
		
			float mB = csum / wB;
			float mF = (sum - csum) / wF;
			float sb = (float)wB * (float)wF * (mF - mB);
		
			// Check if new maximum found
			if (sb > fmax) {
				fmax = sb;
				threshold = i + 1;
			}
		}
		
		return Math.max(2, Math.min(threshold, 254));
	}
}
