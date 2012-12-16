package se.embargo.core.concurrent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public abstract class ProgressTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements DialogInterface.OnCancelListener {
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
            _dialog.setOnCancelListener(this);
        }
        catch (RuntimeException e) {
        	// This may occur if the task is created from a non-UI thread
        	Log.w(TAG, "Failed to create progress dialog", e);
        	throw e;
        }
	}
	
	protected Context getContext() {
		return _dialog.getContext();
	}
	
	public void setMaxProgress(int maxprogress) {
        _dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _dialog.setIndeterminate(false);
        _dialog.setMax(maxprogress);
	}
	
	public void setCancelable(boolean cancelable) {
		_dialog.setCancelable(cancelable);
	}

	/**
	 * Update the current progress.
	 * @remark	Must be called on the UI thread.
	 * @param 	progress	Progress between 0 and maxprogress
	 */
    protected void setProgress(int progress) {
    	_dialog.setProgress(progress);
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
    	Log.i(TAG, "Task was cancelled: " + this);

    	if (_dialog.isShowing()) {
    		try {
    			_dialog.dismiss();
    		}
    		catch (RuntimeException e) {
    			Log.w(TAG, "Failed to close dialog", e);
    		}
    	}
    }
    
    @Override
    public void onCancel(DialogInterface dialog) {
    	cancel(true);
    }
}
