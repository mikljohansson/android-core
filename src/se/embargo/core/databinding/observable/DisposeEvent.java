package se.embargo.core.databinding.observable;

public class DisposeEvent<T> {
	private T _object;
	
	public DisposeEvent(T object) {
		_object = object;
	}
	
	public T getObject() {
		return _object;
	}
}
