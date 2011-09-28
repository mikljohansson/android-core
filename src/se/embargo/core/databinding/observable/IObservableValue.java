package se.embargo.core.databinding.observable;

public interface IObservableValue<T> extends IObservable<T> {
	public T getValue();
	public void setValue(T value);
}
