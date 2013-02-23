package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.ChangeEvent.ChangeType;
import se.embargo.core.databinding.observable.IObservableValue;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WidgetProperties {
	public static IValueProperty<View, Boolean> enabled() {
		return _enabled;
	}

	public static IValueProperty<View, Integer> visible() {
		return _visible;
	}

	public static IValueProperty<TextView, String> text() {
		return _text;
	}

	public static IValueProperty<ProgressBar, Integer> progress() {
		return _progress;
	}

	public static IValueProperty<ImageView, Uri> imageUri() {
		return _imageuri;
	}

	public static IValueProperty<ImageView, Drawable> imageDrawable() {
		return _imagedrawable;
	}

	public static IValueProperty<ImageView, Bitmap> imageBitmap() {
		return _imagebitmap;
	}

	public static IValueProperty<ImageView, Integer> imageResource() {
		return _imageresource;
	}
	
	private static IValueProperty<View, Boolean> _enabled = new ValueProperty<View, Boolean>() {
		@Override
		public Boolean getValue(View object) {
			return object.isEnabled();
		}

		@Override
		public void setValue(View object, final Boolean value) {
			if (value != null) {
				object.setEnabled(value);
			}
		}
	};

	private static IValueProperty<View, Integer> _visible = new ValueProperty<View, Integer>() {
		@Override
		public Integer getValue(View object) {
			return object.getVisibility();
		}

		@Override
		public void setValue(View object, final Integer value) {
			if (value != null) {
				object.setVisibility(value);
			}
		}
	};
	
	private static class ViewFocusValue<ObjectType extends View, ValueType> extends AbstractObservableValue<ValueType> implements OnFocusChangeListener {
		private final IValueProperty<ObjectType, ValueType> _property;
		private final ObjectType _object;
		
		public ViewFocusValue(IValueProperty<ObjectType, ValueType> property, ObjectType object) {
			_property = property;
			_object = object;
			_object.setOnFocusChangeListener(this);
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				fireChangeEvent(new ChangeEvent<ValueType>(ChangeType.Reset, getValue()));
			}
		}

		@Override
		public ValueType getValue() {
			return _property.getValue(_object);
		}

		@Override
		public void setValue(ValueType value) {
			_property.setValue(_object, value);
		}
	}
	
	private static IValueProperty<TextView, String> _text = new ValueProperty<TextView, String>() {
		public IObservableValue<String> observe(final TextView object) {
			return new ViewFocusValue<TextView, String>(this, object);
		}

		@Override
		public String getValue(TextView object) {
			return object.getText().toString();
		}

		@Override
		public void setValue(TextView object, final String value) {
			if (value != null) {
				object.setText(value);
			}
			else {
				object.setText("");
			}
		}
	};

	private static IValueProperty<ProgressBar, Integer> _progress = new ValueProperty<ProgressBar, Integer>() {
		@Override
		public Integer getValue(ProgressBar object) {
			return object.getProgress();
		}

		@Override
		public void setValue(ProgressBar object, final Integer value) {
			if (value != null) {
				object.setProgress(value);
			}
		}
	};

	private static IValueProperty<ImageView, Uri> _imageuri = new ValueProperty<ImageView, Uri>() {
		@Override
		public Uri getValue(ImageView object) {
			return null;
		}

		@Override
		public void setValue(ImageView object, final Uri value) {
			object.setImageURI(null);
			object.setImageURI(value);
		}
	};
	
	private static IValueProperty<ImageView, Drawable> _imagedrawable = new ValueProperty<ImageView, Drawable>() {
		@Override
		public Drawable getValue(ImageView object) {
			return null;
		}

		@Override
		public void setValue(ImageView object, final Drawable value) {
			object.setImageDrawable(null);
			object.setImageDrawable(value);
		}
	};

	private static IValueProperty<ImageView, Bitmap> _imagebitmap = new ValueProperty<ImageView, Bitmap>() {
		@Override
		public Bitmap getValue(ImageView object) {
			return null;
		}

		@Override
		public void setValue(ImageView object, final Bitmap value) {
			object.setImageBitmap(null);
			object.setImageBitmap(value);
		}
	};

	private static IValueProperty<ImageView, Integer> _imageresource = new ValueProperty<ImageView, Integer>() {
		@Override
		public Integer getValue(ImageView object) {
			return -1;
		}

		@Override
		public void setValue(ImageView object, final Integer value) {
			if (value != null) {
				object.setImageResource(value);
			}
			else {
				object.setImageBitmap(null);
			}
		}
	};
}
