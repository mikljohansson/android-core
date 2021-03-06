package se.embargo.core.databinding.observable;

import se.embargo.core.databinding.IViewMapper;
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * Adapts an IObservableList to work with a ListView.
 */
public class ObservableListAdapter<T> extends BaseAdapter implements ListAdapter {
	private IChangeListener<T> _changeListener;
	private IObservableList<T> _source;
	private IViewMapper<T> _mapper;
	private long _listeners = 0;
	
	/**
	 * @param activity	Activity to which the ListView is attached
	 * @param source	List of values to adapt
	 * @param mapper	Maps list items to each View in the ListView
	 */
	public ObservableListAdapter(Activity activity, IObservableList<T> source, IViewMapper<T> mapper) {
		_changeListener = new RealmChangeListener<T>(activity) {
			public void handleEvent(ChangeEvent<T> event) {
				notifyDataSetChanged();
			}
		};

		_source = source;
		_mapper = mapper;
	}

	@Override
	public int getCount() {
		return _source.size();
	}

	@Override
	public Object getItem(int position) {
		return _source.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T item = _source.get(position);
		return _mapper.convert(item, convertView, parent);
	}
	
	@Override
	public int getItemViewType(int position) {
		T item = _source.get(position);
		return _mapper.getItemViewType(item);
	}
	
	@Override
	public int getViewTypeCount() {
		return _mapper.getViewTypeCount();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
		
		if (_listeners++ == 0) {
			_source.addChangeListener(_changeListener);
		}
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
		
		if (--_listeners == 0) {
			_source.removeChangeListener(_changeListener);
		}
	}
}
