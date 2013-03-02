package se.embargo.core.databinding;

public abstract class ViewMapper<T> implements IViewMapper<T> {
	public int getItemViewType(T item) {
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}
}
