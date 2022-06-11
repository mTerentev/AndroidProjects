package com.mycompany.render3d;


import android.app.*;
import android.content.res.*;
import android.opengl.*;
import android.util.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import javax.microedition.khronos.egl.EGLConfig;
import android.transition.*;
import android.renderscript.*;

public class Renderer implements GLSurfaceView.Renderer
{
	private Activity activity;

	Object3D[] scene;
	
    int mPositionHandle;
	int mColorHandle;
	int mNormaleHandle;
	int programHandle;
	float aspect;
	float time;
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
		{
			Log.e("shaders_code","error reading file");
		}
		return result;
	}

    public Renderer(Activity activity, Object3D[] scene)
    {
		this.activity=activity;
		this.scene=scene;
		new Timer().schedule(new mTimerTask(),0,10);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(0.5f, 0.5f, 0.7f, 1.0f);
		//GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final String vertexShader = LoadCodeFile(activity,"shaders/vertex.txt");
        final String fragmentShader = LoadCodeFile(activity,"shaders/fragment.txt");

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShaderHandle, vertexShader);
		GLES20.glCompileShader(vertexShaderHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(vertexShaderHandle));
		Log.i("shaders_code",vertexShader+"\n");
		
		int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
		GLES20.glCompileShader(fragmentShaderHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(fragmentShaderHandle));
		Log.i("shaders_code",fragmentShader+"\n");

        programHandle = GLES20.glCreateProgram();
		GLES20.glAttachShader(programHandle, vertexShaderHandle);
		GLES20.glAttachShader(programHandle, fragmentShaderHandle);
		GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
		GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
		GLES20.glBindAttribLocation(programHandle, 2, "a_Normale");
		GLES20.glLinkProgram(programHandle);

        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
		mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
		mNormaleHandle = GLES20.glGetAttribLocation(programHandle, "a_Normale");

        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
		aspect=(float)width/(float)height;
		GLES20.glViewport(0, 0, width, height);
	}

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		mMatrix VMatrix = new mMatrix(4).Rotate(35,1,0,0)
			.MMultiply(new mMatrix(4).Rotate(-45,0,1,0))
			.MMultiply(new mMatrix(4).Translate(-5,-5,5));
		
		mMatrix PMatrix = new mMatrix(4)
			.Perspective(60,aspect,0.1f,100);

		for(Object3D obj:scene){
			mMatrix AMMatrix = obj.Animate(time,obj.MMatrix);
			mMatrix MVPMatrix = new mMatrix(4)
				.MMultiply(PMatrix)
				.MMultiply(VMatrix)
				.MMultiply(AMMatrix);

			int MVPmatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_MVPmatrix");
			GLES20.glUniformMatrix4fv(MVPmatrixHandle,1,false,MVPMatrix.arr,0);

			float[] n = new float[]{0,5,-5,1};
			n = AMMatrix.inverse().VMultiply(n);
			int LightHandle = GLES20.glGetUniformLocation(programHandle,"u_Light");
			GLES20.glUniform3fv(LightHandle, 1, n, 0);

			n = new float[]{0,0,0,1};
			n = AMMatrix.inverse().MMultiply(VMatrix.inverse()).VMultiply(n);
			int CameraHandle = GLES20.glGetUniformLocation(programHandle,"u_Camera");
			GLES20.glUniform3fv(CameraHandle, 1, n, 0);

			for(FloatBuffer fb: obj.facesb){
				DrawFace(fb);
			}
		}
    }
	
	private void DrawFace(FloatBuffer aPlaneBuffer){
		aPlaneBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
		aPlaneBuffer.position(3);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
		aPlaneBuffer.position(7);
		GLES20.glVertexAttribPointer(mNormaleHandle, 3, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
		GLES20.glEnableVertexAttribArray(mNormaleHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, aPlaneBuffer.capacity()/10);
		//GLES20.glDrawArrays(GLES20.GL_POINTS, 0, aPlaneBuffer.capacity()/10);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mColorHandle);
		GLES20.glDisableVertexAttribArray(mNormaleHandle);
	}
	
	class mTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			time+=0.01;
		}
	}
}

