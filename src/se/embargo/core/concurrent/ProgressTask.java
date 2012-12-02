package se.embargo.core.concurrent;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class ProgressTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private static final String TAG = "ProgressTask";
	private final ProgressDialog _dialog;
	
	public ProgressTask(Context context, int titleId, int messageid) {
        try {
        	_dialog = new ProgressDialog(context);
            _dialog.setTitle(titleId);
            _dialog.setMessage(context.getResources().getString(messageid));
            _dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            _dialog.setIndeterminate(true);
            _dialog.setCancelable(false);
        }
        catch (RuntimeException e) {
        	// This may occur if the task is created from a non-UI thread
        	Log.w(TAG, "Failed to create progress dialog", e);
        	throw e;
        }
	}

    protected void onPreExecute() {
		try {
	   		_dialog.show();
		}
		catch (RuntimeException e) {
			Log.w(TAG, "Failed to show dialog", e);
		}
    }
    
    protected void onPostExecute(Result result) {
    	if (_dialog.isShowing()) {
    		try {
    			_dialog.dismiss();
    		}
    		catch (RuntimeException e) {
    			Log.w(TAG, "Failed to close dialog", e);
    		}
    	}
    }
    
    protected void onCancelled(Result result) {
    	if (_dialog.isShowing()) {
    		try {
    			_dialog.dismiss();
    		}
    		catch (RuntimeException e) {
    			Log.w(TAG, "Failed to close dialog", e);
    		}
    	}
    }
}
