package se.embargo.core.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ServiceHandle {
	private static final String TAG = "ServiceHandle";
	
	private final Context _context;
	private final Class<? extends AbstractService> _serviceClass;
	private final ServiceListener _listener;
    
	private final ServiceConnection _connection = new ServiceHandler();
	private final Messenger _input = new Messenger(new MessageHandler());
	private Messenger _output = null;
    private boolean _bound = false; 
    
    public ServiceHandle(Context context, Class<? extends AbstractService> serviceClass, ServiceListener listener) {
    	_context = context;
    	_serviceClass = serviceClass;
    	_listener = listener;
    	
    	if (checkStatus()) {
    		bind();
    	}
    }

    public boolean isRunning() {
    	return _bound && _output != null;
    }
    
    public void send(Message msg) throws RemoteException {
    	if (_bound && _output != null) {
           	_output.send(msg);
    	}
    }
    
    public void start() {
    	_context.startService(new Intent(_context, _serviceClass));    	
    	bind();
    }
    
    public void stop() {
    	dispose();
    	_context.stopService(new Intent(_context, _serviceClass));
    }
    
    public void dispose() {
        if (_bound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister
            if (_output != null) {
                try {
                    Message msg = Message.obtain(null, AbstractService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = _input;
                    _output.send(msg);
                } 
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed
                }
            }
            
            // Detach our existing connection
            _context.unbindService(_connection);
            _bound = false;
            _output = null;

        	if (_listener != null) {
        		_listener.onServiceDisconnected();
        	}
            
            Log.i(TAG, "Disconnected from service: " + _serviceClass);
        }
    }
    
    private void bind() {
    	_context.bindService(new Intent(_context, _serviceClass), _connection, Context.BIND_AUTO_CREATE);
    	_bound = true;
    }
    
    private boolean checkStatus() {
    	ActivityManager manager = (ActivityManager) _context.getSystemService(Context.ACTIVITY_SERVICE);
	    
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (_serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    
	    return false;
    }
    
    private class ServiceHandler implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder service) {
            _output = new Messenger(service);

            try {
                Message msg = Message.obtain(null, AbstractService.MSG_REGISTER_CLIENT);
                msg.replyTo = _input;
                _output.send(msg);
            } 
            catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
            
        	if (_listener != null) {
        		_listener.onServiceConnected();
        	}

            Log.i(TAG, "Connected to service: " + _serviceClass);
        }

        public void onServiceDisconnected(ComponentName className) {
            _output = null;

        	if (_listener != null) {
        		_listener.onServiceDisconnected();
        	}

            Log.i(TAG, "Disonnected to service: " + _serviceClass);
        }
    };
    
    @SuppressLint("HandlerLeak")
	private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	if (_listener != null) {
        		_listener.handleMessage(msg);
        	}
        }
    }
}
