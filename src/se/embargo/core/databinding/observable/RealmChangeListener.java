package se.embargo.core.databinding.observable;

import android.app.Activity;

/**
 * Change listener that transfers the event to the UI thread
 * @param <T>	The type of item being watched
 */
public abstract class RealmChangeListener<T> implements IChangeListener<T> {
	private Activity _realm;
	
	public RealmChangeListener(Activity realm) {
		_realm = realm;
	}

	@Override
	public final void handleChange(final ChangeEvent<T> event) {
		_realm.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				handleEvent(event);
			}
		});
	}
	
	protected abstract void handleEvent(ChangeEvent<T> event);
}
