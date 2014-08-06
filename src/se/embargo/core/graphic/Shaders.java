package se.embargo.core.graphic;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.util.Log;

public class Shaders {
	private static final String TAG = "Shaders";
	
	public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            String message = op + ": '" + GLU.gluErrorString(error) + "' (" + error + ")";
        	Log.e(TAG, message);
            throw new RuntimeException(message);
        }
    }
}
