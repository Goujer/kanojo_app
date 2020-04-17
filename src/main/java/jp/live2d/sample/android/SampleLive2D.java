package jp.live2d.sample.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class SampleLive2D extends Activity {
    private GLSurfaceView glSurfaceView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.glSurfaceView = new GLSurfaceView(this);
        this.glSurfaceView.setRenderer(new DrawArraysRenderer(this));
        setContentView(this.glSurfaceView);
        Log.d("a", "hello good morning!!");
    }
}
