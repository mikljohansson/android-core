package se.embargo.core.databinding;

import android.view.View;
import android.view.ViewGroup;

/**
 * Maps a data entry to a view object.
 * @param <T>	Type of data entry, e.g. ContentValues or Cursor.
 */
public interface IViewMapper<T> {
	/**
	 * Create a new view.
	 * @param item		Data item to map into view.
	 * @param parent	Parent view to insert created view into.
	 * @return			Returns the newly created view.
	 */
	public View create(T item, ViewGroup parent);
	
	/**
	 * Attempt to convert an existing view.
	 * @param item		Data item to map into view.
	 * @param view		Existing view to convert.
	 * @return			Returns the existing view or a new view if it couldn't be converted.
	 */
	public View convert(T item, View view);
}
