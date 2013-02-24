package se.embargo.core.preference;

import se.embargo.core.R;
import se.embargo.core.widget.INumberPicker;
import se.embargo.core.widget.NumberPicker;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Preference dialog showing HH:MM:SS spinners.
 */
public class TimeIntervalPreference extends DialogPreference {
	private INumberPicker _hours, _minutes, _seconds;
	private View _widget;
	private int _interval = 0;
	
	public TimeIntervalPreference(Context context) {
		this(context, null);
	}

	public TimeIntervalPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.dialogPreferenceStyle);
	}

	public TimeIntervalPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setWidgetLayoutResource(R.layout.time_interval_preference);
		setDialogLayoutResource(R.layout.time_interval_preference_dialog);

		setPositiveButtonText(R.string.btn_set);
		setNegativeButtonText(R.string.btn_cancel);
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

	@Override
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
		
		_hours = (INumberPicker)view.findViewById(R.id.timeIntervalHour);
		_hours.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_hours.setMinValue(0);
		_hours.setMaxValue(24);
		_hours.setValue(getHours());
		
		_minutes = (INumberPicker)view.findViewById(R.id.timeIntervalMinute);
		_minutes.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_minutes.setMinValue(0);
		_minutes.setMaxValue(60);
		_minutes.setValue(getMinutes());

		_seconds = (INumberPicker)view.findViewById(R.id.timeIntervalSecond);
		_seconds.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
		_seconds.setMinValue(0);
		_seconds.setMaxValue(60);
		_seconds.setValue(getSeconds());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			try {
				int hours = _hours.getValue();
				int minutes = _minutes.getValue();
				int seconds = _seconds.getValue();
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
