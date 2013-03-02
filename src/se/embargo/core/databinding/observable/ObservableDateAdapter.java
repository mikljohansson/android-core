package se.embargo.core.databinding.observable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ObservableDateAdapter extends ObservableValueAdapter<String, Long> {
	private final IObservableValue<String> _object;
	
	public ObservableDateAdapter(IObservableValue<String> object) {
		super(object);
		_object = object;
	}

	@Override
	public Long getValue() {
		return null;
	}

	@Override
	public void setValue(Long value) {
		if (value != null) {
			_object.setValue(SimpleDateFormat.getDateTimeInstance().format(new Date(value)));
		}
		else {
			_object.setValue(null);
		}
	}
}
