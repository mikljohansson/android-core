package se.embargo.core.databinding;

import android.view.View;
import android.view.ViewGroup;

public interface IViewMapper<T> {
	public View create(T item, ViewGroup parent);
	public View convert(T item, View view);
}
