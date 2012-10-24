package se.embargo.core.service;

import android.os.Message;

public interface ServiceListener {
	public void onServiceConnected();
	public void onServiceDisconnected();
	public void handleMessage(Message msg);
}
