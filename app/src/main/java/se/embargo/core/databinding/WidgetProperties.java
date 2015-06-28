package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.ChangeEvent.ChangeType;
import se.embargo.core.databinding.observable.IObservableValue;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Observes properties on android.view.View's.
 */
public class WidgetProperties {
	/**
	 * @return	Property describing the enabled state of a view.
	 */
	public static IValueProperty<View, Boolean> enabled() {
		return _enabled;
	}

	/**
	 * @return	Property describing the checked state of a button.
	 */
	public static IValueProperty<CompoundButton, Boolean> checked() {
		return _checked;
	}
	
	/**
	 * @return	Property describing the visibility state of a view.
	 */
	public static IValueProperty<View, Integer> visible() {
		return _visible;
	}

	/**
	 * @return	Property describing the text of a view.
	 */
	public static IValueProperty<TextView, String> text() {
		return _text;
	}

	/**
	 * @return	Property describing the text of a view.
	 */
	public static IValueProperty<TextView, Boolean> nonEmpty() {
		return _nonEmpty;
	}

	/**
	 * @return	Property describing the current progress of a progress bar.
	 */
	public static IValueProperty<ProgressBar, Integer> progress() {
		return _progress;
	}

	/**
	 * @return	Property describing the image URI of an image view.
	 */
	public static IValueProperty<ImageView, Uri> imageUri() {
		return _imageuri;
	}

	/**
	 * @return	Property describing the current drawable of an image view.
	 */
	public static IValueProperty<ImageView, Drawable> imageDrawable() {
		return _imagedrawable;
	}

	/**
	 * @return	Property describing the current bitmap of an image view.
	 */
	public static IValueProperty<ImageView, Bitmap> imageBitmap() {
		return _imagebitmap;
	}

	/**
	 * @return	Property describing the current resource (e.g. R.drawable.*) of a view.
	 */
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

	private static IValueProperty<CompoundButton, Boolean> _checked = new ValueProperty<CompoundButton, Boolean>() {
		@SuppressWarnings({"unchecked", "rawtypes"})
		public IObservableValue<Boolean> observe(final CompoundButton object) {
			return new TextViewValue<Boolean>((IValueProperty)this, object);
		}

		@Override
		public Boolean getValue(CompoundButton object) {
			return object.isChecked();
		}

		@Override
		public void setValue(CompoundButton object, final Boolean value) {
			if (value != null) {
				object.setChecked(value);
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
	
	private static class TextViewValue<ValueType> extends AbstractObservableValue<ValueType> implements TextWatcher {
		private final IValueProperty<TextView, ValueType> _property;
		private final TextView _object;
		
		public TextViewValue(IValueProperty<TextView, ValueType> property, TextView object) {
			_property = property;
			_object = object;
			_object.addTextChangedListener(this);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			fireChangeEvent(new ChangeEvent<ValueType>(ChangeType.Reset, getValue()));
		}

		@Override
		public ValueType getValue() {
			return _property.getValue(_object);
		}

		@Override
		public void setValue(ValueType value) {
			_property.setValue(_object, value);
		}

		@Override
		public void afterTextChanged(Editable s) {}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	}
	
	private static IValueProperty<TextView, String> _text = new ValueProperty<TextView, String>() {
		public IObservableValue<String> observe(final TextView object) {
			return new TextViewValue<String>(this, object);
		}

		@Override
		public String getValue(TextView object) {
			return object.getText().toString();
		}

		@Override
		public void setValue(TextView object, final String value) {
			if (value != null) {
				if (object.getText() == null || !object.getText().equals(value)) {
					object.setText(value);
				}
			}
			else {
				if (object.getText() != null && !object.getText().equals("")) {
					object.setText("");
				}
			}
		}
	};

	private static IValueProperty<TextView, Boolean> _nonEmpty = new ValueProperty<TextView, Boolean>() {
		public IObservableValue<Boolean> observe(final TextView object) {
			return new TextViewValue<Boolean>(this, object);
		}

		@Override
		public Boolean getValue(TextView object) {
			return object.length() > 0;
		}

		@Override
		public void setValue(TextView object, final Boolean value) {}
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
