package jp.co.cybird.barcodekanojoForGAM.activity.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.view.View;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.view.KanojoFrameLayout;

public final class KanojoScreenShot {
    public static final int SCREEN_CAPTURED = 1;

    public static Bitmap get(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap aScreenCacheBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return aScreenCacheBitmap;
    }

    public static void getFromSurface(KanojoFrameLayout layout, Handler handler) {
        layout.takeScreenShot(handler);
    }

    public static class GLSurface {

        public interface OnDispatchDrawListener {
            void onDispatchDraw(Canvas canvas);
        }

        public static void start() {
            kGLScreenShot.start();
        }

        public static void drawFrame(GLSurfaceView view, GL10 gl) {
            kGLScreenShot.drawFrame(view, gl);
        }

        public static void dispatchDraw(Canvas canvas, OnDispatchDrawListener listener) {
            kGLScreenShot.dispatchDraw(canvas, listener);
        }
    }
}
