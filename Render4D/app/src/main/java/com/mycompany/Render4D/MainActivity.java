package com.mycompany.Render4D;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.opengl.*;
import android.os.*;
import android.util.*;

public class MainActivity extends Activity
{
    /** Hold a reference to our GLSurfaceView */
    private GLSurfaceView mGLSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        // Check if the system supports OpenGL ES 3.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000;

        if (supportsEs3)
        {
            // Request an OpenGL ES 3.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(3);

            // Set the renderer to our demo renderer, defined below.
            mGLSurfaceView.setRenderer(new Renderer(this));

		}
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            Log.e("myTag","Error");
			return;
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
