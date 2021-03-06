package se.embargo.core.databinding.observable;

import se.embargo.core.database.Loaders;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

/**
 * Observes the current cursor of an asynchronous cursor loader.
 */
public class ObservableCursorLoader extends WritableValue<Cursor> implements LoaderManager.LoaderCallbacks<Cursor> {
	private final Context _context;
	private final Uri _uri;
	private final String[] _projection;
	private final String _selection;
	private final String[] _selectionArgs;
	
	public ObservableCursorLoader(Activity context, Uri uri, String[] projection, String selection, String[] selectionArgs) {
		_context = context;
		_uri = uri;
		_projection = projection;
		_selection = selection;
		_selectionArgs = selectionArgs;
		context.getLoaderManager().initLoader(Loaders.createSequenceNumber(), null, this);
	}
	
	public ObservableCursorLoader(Activity context, Uri uri, String[] projection) {
		this(context, uri, projection, null, null);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(_context, _uri, _projection, _selection, _selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> obj, Cursor value) {
		if (value.moveToFirst()) {
			setValue(value);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> obj) {}
}
