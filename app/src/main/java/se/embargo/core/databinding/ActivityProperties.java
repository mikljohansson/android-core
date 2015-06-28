package se.embargo.core.databinding;

import android.app.ActionBar;

public class ActivityProperties {
	/**
	 * @return	Property describing the title of an activity.
	 */
	public static IValueProperty<ActionBar, String> title() {
		return _title;
	}

	private static IValueProperty<ActionBar, String> _title = new ValueProperty<ActionBar, String>() {
		@Override
		public String getValue(ActionBar object) {
			return object.getTitle().toString();
		}

		@Override
		public void setValue(ActionBar object, final String value) {
			if (value != null) {
				object.setTitle(value);
			}
		}
	};
}
