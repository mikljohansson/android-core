package se.embargo.core.preference;

import se.embargo.core.R;
import se.embargo.core.widget.NumberPicker;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimeIntervalPreference extends DialogPreference {
	private NumberPicker _hours, _minutes, _seconds;
	private View _widget;
	private int _interval = 0;
	
	public TimeIntervalPreference(Context context) {
		this(context, null);
	}

	public TimeIntervalPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeIntervalPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setWidgetLayoutResource(R.layout.time_interval_preference);
		setDialogLayoutResource(R.layout.time_interval_preference_dialog);

		setPositiveButtonText(R.string.btn_time_set);
		setNegativeButtonText(R.string.btn_time_cancel);
	}
	
	private int getHours() {
		return _interval / (60 * 60);
	}
	
	private int getMinutes() {
		return _interval % (60 * 60) / 60;
	}

	private int getSeconds() {
		return _interval % 60;
	}

	protected View onCreateView(ViewGroup parent) {
		_widget = super.onCreateView(parent);
		initInterval(_widget);
		return _widget;
	}
	
	private void initInterval(View view) {
		TextView hours = (TextView)view.findViewById(R.id.timeIntervalHour);
		hours.setText(String.format("%02d", getHours()));
		
		TextView minutes = (TextView)view.findViewById(R.id.timeIntervalMinute);
		minutes.setText(String.format("%02d", getMinutes()));

		TextView seconds = (TextView)view.findViewById(R.id.timeIntervalSecond);
		seconds.setText(String.format("%02d", getSeconds()));
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		_hours = (NumberPicker)view.findViewById(R.id.timeIntervalHour);
		_hours.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_hours.setRange(0, 24);
		_hours.setCurrent(getHours());
		
		_minutes = (NumberPicker)view.findViewById(R.id.timeIntervalMinute);
		_minutes.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_minutes.setRange(0, 60);
		_minutes.setCurrent(getMinutes());

		_seconds = (NumberPicker)view.findViewById(R.id.timeIntervalSecond);
		_seconds.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_seconds.setRange(0, 60);
		_seconds.setCurrent(getSeconds());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			try {
				int hours = _hours.getCurrent();
				int minutes = _minutes.getCurrent();
				int seconds = _seconds.getCurrent();
				_interval = hours * 60 * 60 + minutes * 60 + seconds;
				String interval = Integer.toString(_interval);
				
				if (callChangeListener(interval)) {
					persistString(interval);
					initInterval(_widget);
				}
			}
			catch (NumberFormatException e) {}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		String interval = null;

		if (restoreValue) {
			if (defaultValue != null) {
				interval = getPersistedString(defaultValue.toString());
			}
			else {
				interval = getPersistedString("0");
			}
		}
		else {
			interval = defaultValue.toString();
		}

		try {
			_interval = Integer.parseInt(interval);
		}
		catch (NumberFormatException e) {}
	}
}
