package se.embargo.core.databinding.observable;

public interface IDisposeListener<T> {
	public void handleDispose(DisposeEvent<T> event);
}
