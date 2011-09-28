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

	private void fireChangeEvent() {
		fireChangeEvent(new ChangeEvent<T>(null));
	}

	public boolean add(T object) {
		_source.add(object);
		fireChangeEvent();
		return true;
	}

	public void add(int location, T object) {
		_source.add(location, object);
		fireChangeEvent();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(Collection collection) {
		boolean result = _source.addAll(collection);
		if (result) {
			fireChangeEvent();
		}
		
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(int location, Collection collection) {
		boolean result = _source.addAll(location, collection);
		if (result) {
			fireChangeEvent();
		}
		
		return result;
	}

	public void clear() {
		_source.clear();
		fireChangeEvent();
	}

	public boolean contains(Object object) {
		return _source.contains(object);
	}

	@SuppressWarnings("rawtypes")
	public boolean containsAll(Collection collection) {
		return _source.containsAll(collection);
	}

	public T get(int location) {
		return _source.get(location);
	}

	public int indexOf(Object object) {
		return _source.indexOf(object);
	}

	public boolean isEmpty() {
		return _source.isEmpty();
	}

	public Iterator<T> iterator() {
		return _source.iterator();
	}

	public int lastIndexOf(Object object) {
		return _source.lastIndexOf(object);
	}

	public ListIterator<T> listIterator() {
		return _source.listIterator();
	}

	public ListIterator<T> listIterator(int location) {
		return _source.listIterator(location);
	}

	public T remove(int location) {
		T result = _source.remove(location);
		fireChangeEvent();
		return result;
	}

	public boolean remove(Object object) {
		boolean result = _source.remove(object);
		if (result) {
			fireChangeEvent();
		}
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	public boolean removeAll(Collection collection) {
		boolean result = _source.removeAll(collection);
		if (result) {
			fireChangeEvent();
		}
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	public boolean retainAll(Collection collection) {
		boolean result = _source.retainAll(collection);
		if (result) {
			fireChangeEvent();
		}
		
		return result;
	}

	public T set(int location, T object) {
		T result = _source.set(location, object);
		fireChangeEvent();
		return result;
	}

	public int size() {
		return _source.size();
	}

	public List<T> subList(int start, int end) {
		return new WritableList<T>(_source.subList(start, end));
	}

	public Object[] toArray() {
		return _source.toArray();
	}

	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] array) {
		return _source.toArray(array);
	}
}
