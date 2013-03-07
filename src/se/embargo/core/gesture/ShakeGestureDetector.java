package se.embargo.core.gesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class ShakeGestureDetector {
	private static final String TAG = "ShakeDetector";
	
	private static final float FORCE_THRESHOLD = 5f;
	
	/**
	 * Max time between successive changes in direction before resetting gesture.
	 */
	private static final long GESTURE_TIMEOUT = 500;
	
	private final SensorManager _manager;
	private final SensorEventListener _listener = new ShakeListener();
	private final int _minShakes = 3;
	
	public ShakeGestureDetector(Context context) {
		_manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		_manager.registerListener(_listener, _manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
	}
	
	public void dispose() {
		_manager.unregisterListener(_listener);
	}
	
	protected abstract void onGestureDetected();
	
	private class ShakeListener implements SensorEventListener {
		private long _timestamp;
		private float _xacc, _yacc, _zacc;
		private boolean _first = true;
		private int _shakes = 0;
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// Check for gesture timeout
			if (_shakes > 0 && (event.timestamp - _timestamp) / 1000000 >= GESTURE_TIMEOUT) {
				_first = true;
				_shakes = 0;
				Log.d(TAG, "Cancelling shake gesture due to timeout");
			}
			
			// Detect change in direction
			if (!_first) {
				float xdelta = Math.abs(_xacc - event.values[0]);
				float ydelta = Math.abs(_yacc - event.values[1]);
				float zdelta = Math.abs(_zacc - event.values[2]);
				
				if ((xdelta > FORCE_THRESHOLD && ydelta > FORCE_THRESHOLD) || (xdelta > FORCE_THRESHOLD && zdelta > FORCE_THRESHOLD) || (ydelta > FORCE_THRESHOLD && zdelta > FORCE_THRESHOLD)) {
					_timestamp = event.timestamp;
					_shakes++;
					Log.d(TAG, "Shake count is: " + _shakes);
				}
			}

			_xacc = event.values[0];
			_yacc = event.values[1];
			_zacc = event.values[2];
			_first = false;
			
			if (_shakes >= _minShakes) {
				Log.i(TAG, "Shake gesture detected");
				_shakes = 0;
				onGestureDetected();
			}
		}
	}
}
