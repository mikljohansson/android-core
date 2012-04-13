package se.embargo.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that draws rounded corners
 */
public class ThumbnailView extends ImageView {
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
        Bitmap bm =  ((BitmapDrawable)drawable).getBitmap() ;
        
        if (bm != null) {
	        Bitmap rounded = getRoundedCorners(bm);
	        canvas.drawBitmap(rounded, 0, 0 , null);
        }
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
}
