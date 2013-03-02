package se.embargo.core.database;

import se.embargo.core.databinding.IViewMapper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Connects the loader manager with a ListView.
 */
public abstract class CursorMapperAdapter extends CursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {
	private final IViewMapper<ContentValues> _mapper;
	
	/**
	 * @param	context	Handle on activity.
	 */
	public CursorMapperAdapter(FragmentActivity context, IViewMapper<ContentValues> mapper) {
		super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        _mapper = mapper;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor cursor = (Cursor)getItem(position);
		if (cursor != null) {
			return _mapper.convert(Cursors.toContentValues(cursor), convertView, parent);
		}
		
		return null;
	}
	
	@Override
	public int getItemViewType(int position) {
		Cursor cursor = (Cursor)getItem(position);
		if (cursor != null) {
			return _mapper.getItemViewType(Cursors.toContentValues(cursor));
		}

		return IGNORE_ITEM_VIEW_TYPE;
	}
	
	@Override
	public int getViewTypeCount() {
		return _mapper.getViewTypeCount();
	}
	
	@Override
	public final View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}
	
	@Override
	public final void bindView(View view, Context context, Cursor cursor) {}
}
