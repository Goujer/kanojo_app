package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.activity.util.KanojoScreenShot;

public abstract class KanojoGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    public KanojoGLSurfaceView(Context context) {
        super(context);
    }

    public KanojoGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        KanojoScreenShot.GLSurface.drawFrame(this, gl);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        KanojoScreenShot.GLSurface.dispatchDraw(canvas, new KanojoScreenShot.GLSurface.OnDispatchDrawListener() {
            public void onDispatchDraw(Canvas canvas) {
                KanojoGLSurfaceView.super.dispatchDraw(canvas);
            }
        });
    }
}
