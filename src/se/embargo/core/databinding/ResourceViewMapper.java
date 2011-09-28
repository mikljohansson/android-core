package se.embargo.core.databinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceViewMapper<T> implements IViewMapper<T> {
	private int _resource;
	
	public ResourceViewMapper(int resource) {
		_resource = resource;
	}
	
	public View create(T item, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(_resource, parent, false);
		return convert(item, view);
	}

	public View convert(T item, View view) {
		map(item, view);
		return view;
	}

	public abstract void map(T item, View view);
}
