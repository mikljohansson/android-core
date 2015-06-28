package se.embargo.core.databinding;

import android.view.View;
import android.view.ViewGroup;

/**
 * Maps a data entry to a view object.
 * @param <T>	Type of data entry, e.g. ContentValues or Cursor.
 */
public interface IViewMapper<T> {
	/**
	 * Attempt to convert an existing view.
	 * @param item		Data item to map into view.
	 * @param view		Existing view to convert, null if this is the first time.
	 * @param parent	Parent view to insert created view into if the existing one couldn't be converted.
	 * @return			Returns the existing view or a new view if it couldn't be converted.
	 */
	public View convert(T item, View view, ViewGroup parent);

	/**
	 * Get the type of View that will be created for the specific item.
	 */
	public int getItemViewType(T item);

	/**
	 * Returns the number of types of Views that will be created.
	 */
	public int getViewTypeCount();
}
