package se.embargo.core.graphics;

import java.io.IOException;
import java.io.InputStream;

import se.embargo.core.io.Streams;
import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class ShaderProgram {
    private static final String TAG = "ShaderProgram";
	
	private Context _context;
    private int _program;

    public ShaderProgram(Context context, int vertexShader, int fragmentShader) {
		_context = context;
		_program = createProgram(vertexShader, fragmentShader);
	}
	
    public void draw() {
        GLES20.glUseProgram(_program);
        checkGlError("glUseProgram");
    }
    
	public int getAttributeLocation(String name) {
		int handle = GLES20.glGetAttribLocation(_program, name);
        checkGlError("glGetAttribLocation " + name);
        if (handle == -1) {
            throw new RuntimeException("Could not get attrib location for " + name);
        }
        
        return handle;
	}
    
	public int getUniformLocation(String name) {
		int handle = GLES20.glGetUniformLocation(_program, name);
        checkGlError("glGetUniformLocation " + name);
        if (handle == -1) {
            throw new RuntimeException("Could not get uniform location for " + name);
        }
        
        return handle;
	}

	private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
	
    private int loadShader(int shaderType, int sourceid) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            // Read the shader source from file
        	InputStream is = _context.getResources().openRawResource(sourceid);
            String source;
			try {
				source = Streams.toString(is);
			}
			catch (IOException e) {
				return 0;
			}
        	
        	// Compile shader
            int[] compileStatus = new int[1];
			GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        
        return shader;
    }

    private int createProgram(int vertexid, int fragmentid) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexid);
        if (vertexShader == 0) {
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentid);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            
            int[] linkStatus = new int[1];
            GLES20.glLinkProgram(program);
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        
        return program;
    }
}
