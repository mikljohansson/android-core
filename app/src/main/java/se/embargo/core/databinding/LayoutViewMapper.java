package se.embargo.core.databinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Uses a layout to create views.
 * @param <T>	Type of data entry, e.g. ContentValues or Cursor.
 */
public abstract class LayoutViewMapper<T> extends ViewMapper<T> {
	private int _resource;
	
	/**
	 * @param resource	Id of layout to inflate, e.g. R.layout.entry_listitem
	 */
	public LayoutViewMapper(int resource) {
		_resource = resource;
	}
	
	@Override
	public View convert(T item, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(_resource, parent, false);
		}
		
		return view;
	}
}
