package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.IObservableValue;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WidgetProperties {
	public static IValueProperty<View, Boolean> enabled() {
		return _enabled;
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

	private static IValueProperty<View, Boolean> _enabled = new IValueProperty<View, Boolean>() {
		public IObservableValue<Boolean> observe(final View object) {
			return new AbstractObservableValue<Boolean>() {
				public Boolean getValue() {
					return object.isEnabled();
				}

				public void setValue(Boolean value) {
					object.setEnabled(value);
				}
			};
		}
	};

	private static IValueProperty<TextView, String> _text = new IValueProperty<TextView, String>() {
		public IObservableValue<String> observe(final TextView object) {
			return new AbstractObservableValue<String>() {
				public String getValue() {
					return object.getText().toString();
				}

				public void setValue(String value) {
					object.setText(value);
				}
			};
		}
	};

	private static IValueProperty<ProgressBar, Integer> _progress = new IValueProperty<ProgressBar, Integer>() {
		public IObservableValue<Integer> observe(final ProgressBar object) {
			return new AbstractObservableValue<Integer>() {
				public Integer getValue() {
					return object.getProgress();
				}

				public void setValue(Integer value) {
					object.setProgress(value);
				}
			};
		}
	};

	private static IValueProperty<ImageView, Uri> _imageuri = new IValueProperty<ImageView, Uri>() {
		public IObservableValue<Uri> observe(final ImageView object) {
			return new AbstractObservableValue<Uri>() {
				public Uri getValue() {
					return null;
				}

				public void setValue(Uri value) {
					object.setImageURI(null);
					object.setImageURI(value);
				}
			};
		}
	};
	
	private static IValueProperty<ImageView, Drawable> _imagedrawable = new IValueProperty<ImageView, Drawable>() {
		public IObservableValue<Drawable> observe(final ImageView object) {
			return new AbstractObservableValue<Drawable>() {
				public Drawable getValue() {
					return null;
				}

				public void setValue(Drawable value) {
					object.setImageDrawable(null);
					object.setImageDrawable(value);
				}
			};
		}
	};

	private static IValueProperty<ImageView, Bitmap> _imagebitmap = new IValueProperty<ImageView, Bitmap>() {
		public IObservableValue<Bitmap> observe(final ImageView object) {
			return new AbstractObservableValue<Bitmap>() {
				public Bitmap getValue() {
					return null;
				}

				public void setValue(Bitmap value) {
					object.setImageBitmap(null);
					object.setImageBitmap(value);
				}
			};
		}
	};

	private static IValueProperty<ImageView, Integer> _imageresource = new IValueProperty<ImageView, Integer>() {
		public IObservableValue<Integer> observe(final ImageView object) {
			return new AbstractObservableValue<Integer>() {
				public Integer getValue() {
					return -1;
				}

				public void setValue(Integer value) {
					object.setImageResource(value);
				}
			};
		}
	};
}
