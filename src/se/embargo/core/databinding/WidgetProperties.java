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

	private static IValueProperty<View, Boolean> _enabled = new IValueProperty<View, Boolean>() {
		public IObservableValue<Boolean> observe(final View object) {
			return new AbstractObservableValue<Boolean>() {
				public Boolean getValue() {
					return object.isEnabled();
				}

				public void setValue(final Boolean value) {
					if (value != null) {
						object.post(new Runnable() {
							public void run() {
								object.setEnabled(value);
							}
						});
					}
				}
			};
		}
	};

	private static IValueProperty<View, Integer> _visible = new IValueProperty<View, Integer>() {
		public IObservableValue<Integer> observe(final View object) {
			return new AbstractObservableValue<Integer>() {
				public Integer getValue() {
					return object.getVisibility();
				}

				public void setValue(final Integer value) {
					if (value != null) {
						object.post(new Runnable() {
							public void run() {
								object.setVisibility(value);
							}
						});
					}
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

				public void setValue(final String value) {
					object.post(new Runnable() {
						public void run() {
							if (value != null) {
								object.setText(value);
							}
							else {
								object.setText("");
							}
						}
					});
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

				public void setValue(final Integer value) {
					if (value != null) {
						object.post(new Runnable() {
							public void run() {
								object.setProgress(value);
							}
						});
					}
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

				public void setValue(final Uri value) {
					object.post(new Runnable() {
						public void run() {
							object.setImageURI(null);
							object.setImageURI(value);
						}
					});
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

				public void setValue(final Drawable value) {
					object.post(new Runnable() {
						public void run() {
							object.setImageDrawable(null);
							object.setImageDrawable(value);
						}
					});
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

				public void setValue(final Bitmap value) {
					object.post(new Runnable() {
						public void run() {
							object.setImageBitmap(null);
							object.setImageBitmap(value);
						}
					});
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

				public void setValue(final Integer value) {
					object.post(new Runnable() {
						public void run() {
							if (value != null) {
								object.setImageResource(value);
							}
							else {
								object.setImageBitmap(null);
							}
						}
					});
				}
			};
		}
	};
}
