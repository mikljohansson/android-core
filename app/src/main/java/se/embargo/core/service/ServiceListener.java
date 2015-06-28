package se.embargo.core.service;

import android.os.Message;

/**
 * Interface to receiving service messages and state changes.
 * 
 * Implement this interface in the UI thread and connect it to a service handle in order to safely receive 
 * messages from the background service. All methods on this interface are executed in the UI thread so there's
 * no need to explicitly transfer control to the foreground.
 */
public interface ServiceListener {
	/**
	 * The service was connected to the service handle.
	 */
	public void onServiceConnected();
	
	/**
	 * The service disconnected from the service handle, e.g. it was stopped.
	 */
	public void onServiceDisconnected();
	
	/**
	 * Handle a message from the background service.
	 * @param	msg	Message received from service.
	 */
	public void handleMessage(Message msg);
}
