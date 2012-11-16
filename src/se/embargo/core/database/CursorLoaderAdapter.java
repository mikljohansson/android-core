package se.embargo.core.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public abstract class CursorLoaderAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {
	private final int _resid;
	
	public CursorLoaderAdapter(SherlockFragmentActivity context, int resid) {
		super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		_resid = resid;
        context.getSupportLoaderManager().initLoader(Loaders.createSequenceNumber(), null, this);
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