package se.embargo.core.database;

import android.content.Context;
import android.database.Cursor;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Connects the loader manager with a ListView.
 */
public abstract class CursorLoaderAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {
	private final int _resid;
	
	/**
	 * @param	context	Handle on activity.
	 * @param	resid	Layout to use for ListView rows, e.g. R.layout.*.
	 */
	public CursorLoaderAdapter(Activity context, int resid) {
		super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		_resid = resid;
        context.getLoaderManager().initLoader(Loaders.createSequenceNumber(), null, this);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		swapCursor(null);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(_resid, parent, false);
		bindView(view, context, cursor);
		return view;
	}
}
