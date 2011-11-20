package se.embargo.core.databinding.observable;


public interface IChangeListener<T> {
	public void handleChange(ChangeEvent<T> event);
}
