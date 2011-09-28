package se.embargo.core.databinding.observable;

public interface IObservable<T> {
	public void addChangeListener(IChangeListener<T> listener);
	public void removeChangeListener(IChangeListener<T> listener);
}
