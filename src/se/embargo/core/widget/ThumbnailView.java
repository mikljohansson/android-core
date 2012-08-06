package se.embargo.core.widget;

import se.embargo.core.graphics.Bitmaps;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that draws rounded corners
 */
public class ThumbnailView extends ImageView {
	private static final Paint FILTER_BITMAP_PAINT = new Paint(Paint.FILTER_BITMAP_FLAG);

	public ThumbnailView(Context context) {
		super(context);
	}

	public ThumbnailView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ThumbnailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
	        Bitmap bm =  ((BitmapDrawable)drawable).getBitmap() ;
	        if (bm != null) {
		        Bitmap thumb = Bitmaps.fit(bm, getMeasuredWidth(), getMeasuredHeight());
	        	Bitmap rounded = Bitmaps.getRoundedCorners(thumb);
		        canvas.drawBitmap(rounded, 0, 0 , FILTER_BITMAP_PAINT);
	        }
        }
	}
}
