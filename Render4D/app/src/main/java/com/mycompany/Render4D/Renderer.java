package com.mycompany.Render4D;

import android.opengl.*;
import android.util.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import javax.microedition.khronos.egl.EGLConfig;
import android.content.res.*;
import android.app.*;

public class Renderer implements GLSurfaceView.Renderer
{
	private Activity activity;

    private final FloatBuffer planeVertices;

    private int mPositionHandle;
	private int programHandle;
	private int width;
	private int height;
	private float time;
    private final int mBytesPerFloat = 4;

	private String LoadCodeFile(Activity activity,String file){
		AssetManager as = activity.getAssets();
		String result = "";
		try
		{
			InputStream inputStream = as.open(file);
			Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			while(s.hasNext()){result += s.next();}
		}
		catch (IOException e)
		{}
		return result;
	}
	
    public Renderer(Activity activity)
    {
		this.activity=activity;
		final float[] planeVerticesData = {
			-1, -1,
			1, -1,
			1, 1,
			-1, 1,
		};

        // Initialize the buffers.
        planeVertices = ByteBuffer.allocateDirect(planeVerticesData.length * mBytesPerFloat)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();

        planeVertices.put(planeVerticesData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(0.5f, 0.5f, 0.7f, 1.0f);
		
        final String vertexShader = LoadCodeFile(activity,"shaders/vertex.txt");
        final String fragmentShader = LoadCodeFile(activity,"shaders/fragment.txt");

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);
			Log.e("shaders",GLES20.glGetShaderInfoLog(vertexShaderHandle));

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);
			Log.e("shaders",GLES20.glGetShaderInfoLog(fragmentShaderHandle));

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        // Set program handles. These will later be used to pass in values to the program.
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
		//mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
		this.width=width;
		this.height=height;
        GLES20.glViewport(0, 0, width, height);
		int mResolutionHandle = GLES20.glGetUniformLocation(programHandle,"resolution");
		GLES20.glUniform2f(mResolutionHandle,width,height);
	}

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		time+=0.01f;
        int mTimeHandle = GLES20.glGetUniformLocation(programHandle,"time");
		GLES20.glUniform1f(mTimeHandle,time*3f);
		
        drawPlane(planeVertices);
    }
	
    private void drawPlane(final FloatBuffer aPlaneBuffer)
    {
        // Pass in the position information
        aPlaneBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 2*mBytesPerFloat, aPlaneBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }
}
