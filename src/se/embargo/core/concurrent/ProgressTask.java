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
        }
        catch (RuntimeException e) {
        	Log.e(TAG, "Failed to create progress dialog", e);
        	throw e;
        }
        
        _dialog.setTitle(titleId);
        _dialog.setMessage(context.getResources().getString(messageid));
        _dialog.setIndeterminate(true);
        _dialog.setCancelable(false);
	}

    protected void onPreExecute() {
        _dialog.show();
    }
    
    protected void onPostExecute(Result result) {
    	if (_dialog.isShowing()) {
    		_dialog.dismiss();
    	}
    }
    
    protected void onCancelled(Result result) {
    	if (_dialog.isShowing()) {
    		_dialog.dismiss();
    	}
    }
}
