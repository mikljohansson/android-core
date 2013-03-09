package se.embargo.core.widget;

import se.embargo.core.R;
import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class SelectionActionMode implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, ActionMode.Callback {
	private final SherlockFragmentActivity _activity;
	private final ListView _listview;
	private final int _menuResource;
	private ActionMode _mode = null;
	private AdapterView.OnItemClickListener _prevItemClickListener = null;
	
	public SelectionActionMode(SherlockFragmentActivity activity, ListView listview, int menuResource) {
		_activity = activity;
		_listview = listview;
		_menuResource = menuResource;
	}
	
	protected Activity getActivity() {
		return _activity;
	}
	
	protected ListView getListView() {
		return _listview;
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View item, int position, long id) {
		_mode = _activity.startActionMode(this);
		_mode.setTitle(_activity.getResources().getString(R.string.selection_actionmode_title, 1));
		_prevItemClickListener = _listview.getOnItemClickListener();
		_listview.setOnItemClickListener(this);
		_listview.setOnItemLongClickListener(null);
		_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
		refresh();
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(_menuResource, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		_mode = null;
		_listview.setOnItemClickListener(_prevItemClickListener);
		_listview.setOnItemLongClickListener(this);
		
		// Deselect all items when exiting action mode
		SparseBooleanArray items = _listview.getCheckedItemPositions();
        for (int i = 0; i < items.size(); i++) {
        	int position = items.keyAt(i);
        	if (items.get(position)) {
        		_listview.setItemChecked(position, false);
        	}
        }
        
        // Circumvent problem with list view not redrawing when switching to CHOICE_MODE_NONE
        _listview.post(new Runnable() {
            @Override
            public void run() {
            	_listview.clearChoices();
            	_listview.setChoiceMode(ListView.CHOICE_MODE_NONE);
            }
        });
	}
	
	private void refresh() {
        // Count the number of selected items
		int count = 0;
        SparseBooleanArray items = _listview.getCheckedItemPositions();
        for (int i = 0; i < items.size(); i++) {
        	if (items.get(items.keyAt(i))) {
        		count++;
        	}
        }
		
        // Exit the action mode when count becomes zero
        if (count > 0) {
        	_mode.setTitle(_activity.getResources().getString(R.string.selection_actionmode_title, count));
        }
        else {
        	_mode.finish();
        }
	}
}
