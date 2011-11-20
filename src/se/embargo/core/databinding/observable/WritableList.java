package se.embargo.core.databinding.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WritableList<T> extends AbstractObservable<T> implements IObservableList<T> {
	private List<T> _source;

	public WritableList() {
		this(new ArrayList<T>());
	}
	
	private WritableList(List<T> source) {
		_source = source;
	}

	public synchronized boolean add(T object) {
		_source.add(object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
		return true;
	}

	public synchronized void add(int location, T object) {
		_source.add(location, object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized boolean addAll(Collection collection) {
		boolean result = _source.addAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized boolean addAll(int location, Collection collection) {
		boolean result = _source.addAll(location, collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	public synchronized void clear() {
		_source.clear();
		fireChangeEvent(new ChangeEvent<T>());
	}

	public synchronized boolean contains(Object object) {
		return _source.contains(object);
	}

	@SuppressWarnings("rawtypes")
	public synchronized boolean containsAll(Collection collection) {
		return _source.containsAll(collection);
	}

	public synchronized T get(int location) {
		return _source.get(location);
	}

	public synchronized int indexOf(Object object) {
		return _source.indexOf(object);
	}

	public synchronized boolean isEmpty() {
		return _source.isEmpty();
	}

	public synchronized Iterator<T> iterator() {
		return _source.iterator();
	}

	public synchronized int lastIndexOf(Object object) {
		return _source.lastIndexOf(object);
	}

	public synchronized ListIterator<T> listIterator() {
		return _source.listIterator();
	}

	public synchronized ListIterator<T> listIterator(int location) {
		return _source.listIterator(location);
	}

	public synchronized T remove(int location) {
		T result = _source.remove(location);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Remove, result));
		return result;
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean remove(Object object) {
		boolean result = _source.remove(object);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Remove, (T)object));
		}
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	public synchronized boolean removeAll(Collection collection) {
		boolean result = _source.removeAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	public synchronized boolean retainAll(Collection collection) {
		boolean result = _source.retainAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	public synchronized T set(int location, T object) {
		T result = _source.set(location, object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
		return result;
	}

	public synchronized int size() {
		return _source.size();
	}

	public synchronized List<T> subList(int start, int end) {
		return new WritableList<T>(_source.subList(start, end));
	}

	public synchronized Object[] toArray() {
		return _source.toArray();
	}

	@SuppressWarnings("unchecked")
	public synchronized Object[] toArray(Object[] array) {
		return _source.toArray(array);
	}
}
