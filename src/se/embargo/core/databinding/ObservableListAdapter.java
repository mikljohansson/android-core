package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.IChangeListener;
import se.embargo.core.databinding.observable.IObservableList;
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class ObservableListAdapter<T> extends BaseAdapter implements ListAdapter {
	private Activity _activity;
	private IObservableList<T> _source;
	private IViewMapper<T> _provider;
	private long _listeners = 0;
	
	private IChangeListener<T> _changeListener = new IChangeListener<T>() {
		public void handleChange(ChangeEvent<T> event) {
			_activity.runOnUiThread(new Runnable() {
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
	};
	
	public ObservableListAdapter(Activity activity, IObservableList<T> source, IViewMapper<T> provider) {
		_activity = activity;
		_source = source;
		_provider = provider;
	}

	public int getCount() {
		return _source.size();
	}

	public Object getItem(int position) {
		return _source.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		T item = _source.get(position);
		if (convertView == null) {
			return _provider.create(item, parent);
		}
		
		return _provider.convert(item, convertView);
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
