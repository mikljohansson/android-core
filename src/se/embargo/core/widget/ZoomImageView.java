package se.embargo.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Image view with pinch to zoom capability.
 */
public class ZoomImageView extends ImageView {
	enum ZoomState { NONE, DRAG, ZOOM };

	/**
	 * Current transformation matrix.
	 */
	private Matrix _matrix = new Matrix();
	
	/**
	 * Previous transformation matrix.
	 */
	private Matrix _prevmatrix = new Matrix();

	/**
	 * Current touch state.
	 */
	private ZoomState _state = ZoomState.NONE;

	/**
	 * Move gesture starting points.
	 */
	private PointF _start = new PointF(), _middle = new PointF();
	
	/**
	 * Distance between fingers when starting pinch gesture.
	 */
	private float _prevdistance = 1f;
	
	/**
	 * Temp buffer for matrix values.
	 */
	private float[] _values = new float[9], _prevvalues = new float[9];

	private float _zoommin = 1.0f, _zoommax = 4.0f, _startzoom = 1.0f;
	
	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ScaleType.MATRIX);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		_matrix.setRectToRect(
			new RectF(0, 0, (float)bm.getWidth(), (float)bm.getHeight()),
			new RectF(0, 0, (float)getWidth(), (float)getHeight()), 
			Matrix.ScaleToFit.CENTER);
		
		_matrix.getValues(_values);
		_zoommin *= _values[Matrix.MSCALE_X];
		_zoommax *= _values[Matrix.MSCALE_X];
		_startzoom = _values[Matrix.MSCALE_X];
		
		setImageMatrix(_matrix);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		 * Matrix values are
		 * 
		 *  [MSCALE_X MSKEW_X MTRANS_X]
		 *  [MSKEW_Y MSCALE_Y MTRANS_Y]
		 *  [MPERSP_0 MPERSP_1 MPERSP_2]
		 */
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				_prevmatrix.set(_matrix);
				_start.set(event.getX(), event.getY());
				_state = ZoomState.DRAG;
				break;

			case MotionEvent.ACTION_POINTER_DOWN:
				_prevdistance = spacing(event);
				if (_prevdistance > 10f) {
					_prevmatrix.set(_matrix);
					midPoint(_middle, event);
					_state = ZoomState.ZOOM;
				}
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				_state = ZoomState.NONE;
				break;

			case MotionEvent.ACTION_MOVE:
				if (_state == ZoomState.DRAG) {
					float px = event.getX() - _start.x, 
						  py = event.getY() - _start.y;
					
					_matrix.getValues(_prevvalues);
					_matrix.set(_prevmatrix);
					_matrix.postTranslate(px, py);
					
					// Check that image is wan't moved incorrectly
					_matrix.getValues(_values);
					
					float w = getWidth(),
						  h = getHeight();

					float px2 = _values[Matrix.MTRANS_X] + w * (_values[Matrix.MSCALE_X] - _startzoom),
						  py2 = _values[Matrix.MTRANS_Y] + h * (_values[Matrix.MSCALE_Y] - _startzoom);

					if (_values[Matrix.MTRANS_X] > 0.0f || px2 < w) {
						_values[Matrix.MTRANS_X] = _prevvalues[Matrix.MTRANS_X];
						_matrix.setValues(_values);
					}
					
					if (_values[Matrix.MTRANS_Y] > 0.0f || py2 < h) {
						_values[Matrix.MTRANS_Y] = _prevvalues[Matrix.MTRANS_Y];
						_matrix.setValues(_values);
					}
				}
				else if (_state == ZoomState.ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						float scale = newDist / _prevdistance;
						_matrix.getValues(_prevvalues);
						_matrix.set(_prevmatrix);
						_matrix.postScale(scale, scale, _middle.x, _middle.y);
						
						// Apply bounds on zoom
						_matrix.getValues(_values);
						
						if (_values[Matrix.MSCALE_X] < _zoommin || _values[Matrix.MSCALE_X] > _zoommax ||
							_values[Matrix.MSCALE_Y] < _zoommin || _values[Matrix.MSCALE_Y] > _zoommax) {
							_matrix.setValues(_prevvalues);
						}
						else {
							if (_values[Matrix.MTRANS_X] > 0.0f) {
								_values[Matrix.MTRANS_X] = 0;
								_matrix.setValues(_values);
							}
							
							if (_values[Matrix.MTRANS_Y] > 0.0f) {
								_values[Matrix.MTRANS_Y] = 0;
								_matrix.setValues(_values);
							}

							float w = getWidth(),
								  h = getHeight();

							float px2 = _values[Matrix.MTRANS_X] + w * (_values[Matrix.MSCALE_X] - _startzoom),
								  py2 = _values[Matrix.MTRANS_Y] + h * (_values[Matrix.MSCALE_Y] - _startzoom);
							
							if (_values[Matrix.MTRANS_X] < 0.0f && px2 < w) {
								_values[Matrix.MTRANS_X] += (w - px2);
								_matrix.setValues(_values);
							}
							
							if (_values[Matrix.MTRANS_Y] < 0.0f && py2 < h) {
								_values[Matrix.MTRANS_Y] += (h - py2);
								_matrix.setValues(_values);
							}
						}
					}
				}
				break;
		}

		setImageMatrix(_matrix);
		return true;
	}

	/** 
	 * Determine the space between the first two fingers 
	 */
	private static float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** 
	 * Calculate the mid point of the first two fingers 
	 */
	private static void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
