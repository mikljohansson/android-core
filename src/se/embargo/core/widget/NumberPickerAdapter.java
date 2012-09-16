package se.embargo.core.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

public class NumberPickerAdapter extends NumberPicker implements INumberPicker {
    @TargetApi(11)
	public NumberPickerAdapter(Context context) {
		super(context);
	}

    @TargetApi(11)
    public NumberPickerAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @TargetApi(11)
	public NumberPickerAdapter(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    }

	@Override
	@TargetApi(11)
	public void setFormatter(final INumberPicker.Formatter formatter) {
		setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return formatter.format(value);
			}
		});
	}
}
