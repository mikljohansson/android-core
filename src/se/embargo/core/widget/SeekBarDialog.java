package se.embargo.core.widget;

import se.embargo.core.R;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.IChangeListener;
import se.embargo.core.databinding.observable.IObservableValue;
import se.embargo.core.databinding.observable.ObservableValueAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarDialog implements DialogInterface.OnDismissListener, SeekBar.OnSeekBarChangeListener {
	private final Activity _context;
	private final IObservableValue<Integer> _value;	
	private final float _step;
	private final int _max, _min;
	private String _format = "%.2f";
	private DialogInterface.OnDismissListener _dismissListener = null;
	private int _layoutid = R.layout.seekbar_dialog;

	public SeekBarDialog(Activity context, IObservableValue<Integer> value, float step, int min, int max) {
		_context = context;
		_value = value;
		_step = step;
		_max = max;
		_min = min;
	}
	
	public SeekBarDialog(Activity context, IObservableValue<Float> value, float step, float min, float max) {
		this(context, new FloatAdapter(value, step), step, (int)(min / step), (int)(max / step));
	}
	
	public void setLayoutResource(int layoutid) {
		_layoutid = layoutid;
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
		_dismissListener = listener;
	}
	
	public void setFormat(String format) {
		_format = format;
	}
	
	public void show() {
		final FrameLayout frameview = new FrameLayout(_context);
		LayoutInflater inflater = _context.getLayoutInflater();
		inflater.inflate(_layoutid, frameview);
		
		build(frameview);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setView(frameview);

		AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(this);
		dialog.setCanceledOnTouchOutside(true);

		// Avoid dimming the background when the dialog is shown
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		// Show the dialog
		dialog.show();
	}

	protected void build(final View parent) {
		final SeekBar exposureview = (SeekBar)parent.findViewById(R.id.valueControl);
		exposureview.setMax(Math.abs(_max - _min));
		exposureview.setProgress(_value.getValue() - _min);
		exposureview.setOnSeekBarChangeListener(this);

		/*
		TextView minview = (TextView)parent.findViewById(R.id.minValue);
		minview.setText(String.format(_format, _step * _min));
		
		TextView maxview = (TextView)parent.findViewById(R.id.maxValue);
		maxview.setText(String.format(_format, _step * _max));
		*/

		final TextView valueview = (TextView)parent.findViewById(R.id.value);
		valueview.setText(String.format(_format, _step * _value.getValue()));
		_value.addChangeListener(new IChangeListener<Integer>() {
			@Override
			public void handleChange(ChangeEvent<Integer> event) {
				int progress = event.getValue() - _min;
				valueview.setText(String.format(_format, _step * event.getValue()));
				
				if (exposureview.getProgress() != progress) {
					exposureview.setProgress(progress);
				}
			}
		});
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (_dismissListener != null) {
			_dismissListener.onDismiss(dialog);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			_value.setValue(progress + _min);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
	
	private static class FloatAdapter extends ObservableValueAdapter<Float, Integer> {
		private final IObservableValue<Float> _object;
		private final float _step;
		
		public FloatAdapter(IObservableValue<Float> object, float step) {
			super(object);
			_object = object;
			_step = step;
		}

		@Override
		public Integer getValue() {
			return (int)(_object.getValue() / _step);
		}

		@Override
		public void setValue(Integer value) {
			_object.setValue((float)value * _step);
		}
	}
}
