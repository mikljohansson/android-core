package se.embargo.core.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Abstract background service.
 * 
 * The service classes allows creating background services that can be communicated with
 * and controlled from the UI thread using message passing.
 */
public abstract class AbstractService extends Service {
	private static final String TAG = "AbstractService";
	
	static final int MSG_REGISTER_CLIENT = 9991;
	static final int MSG_UNREGISTER_CLIENT = 9992;
	static final int MSG_SERVICE_STOPPING = 9993;

	/**
	 * Input message queue.
	 */
	private final Messenger _input = new Messenger(new IncomingHandler());
	
	/**
	 * Currently registered clients.
	 */
	private final ArrayList<Messenger> _clients = new ArrayList<Messenger>();
	
	/**
	 * Flag indicating if service is stopped.
	 */
	private boolean _stopped = false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		_stopped = false;
		
		// Run until explicitly stopped.
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return _input.getBinder();
	}
	
	protected void stopService() {
		Log.i(TAG, "Service requested to stop itself");
		_stopped = true;
		stopSelf();
		send(Message.obtain(null, MSG_SERVICE_STOPPING));
	}
	
	protected void send(Message msg) {
		 for (int i= _clients.size() - 1; i >= 0; i--) {
			try {
				Log.i(TAG, "Sending message to clients: "+msg);
				_clients.get(i).send(msg);
			}
			catch (RemoteException e) {
				Log.w(TAG, "Failed to signal client", e);
				_clients.remove(i);
			}
		}
	}

	protected void onReceiveMessage(Message msg) {}

	/**
	 * Handler of incoming messages from clients.
	 */
	private class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REGISTER_CLIENT:
					Log.i(TAG, "Client registered: "+msg.replyTo);
					try {
						if (_stopped) {
							msg.replyTo.send(Message.obtain(null, MSG_SERVICE_STOPPING));
						}

						_clients.add(msg.replyTo);
					}
					catch (Exception e) {
						Log.w(TAG, "Failed to signal client", e);
					}
					break;
				
				case MSG_UNREGISTER_CLIENT:
					Log.i(TAG, "Client un-registered: "+msg.replyTo);
					_clients.remove(msg.replyTo);
					break;
					
				default:
					onReceiveMessage(msg);
			}
		}
	}
}
