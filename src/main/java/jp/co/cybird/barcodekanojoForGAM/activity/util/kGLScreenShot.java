package jp.co.cybird.barcodekanojoForGAM.activity.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.activity.util.KanojoScreenShot;

public final class kGLScreenShot {
    public static final String TAG = "GLScreenShot";
    private static Bitmap mBitmap;
    private static boolean mTakingGLSurface = false;

    public static void start() {
        mTakingGLSurface = true;
    }

    public static void drawFrame(GLSurfaceView view, GL10 gl) {
        if (mTakingGLSurface) {
            mBitmap = getPixelsInBitmap(0, 0, view.getWidth(), view.getHeight(), gl);
            mTakingGLSurface = false;
        }
    }

    public static void dispatchDraw(Canvas canvas, KanojoScreenShot.GLSurface.OnDispatchDrawListener listener) {
        if (!mTakingGLSurface) {
            listener.onDispatchDraw(canvas);
            return;
        }
        for (int i = 0; i < 100 && mTakingGLSurface; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Log.d(TAG, "Exception Occured during waiting ScreenShot: " + e.toString());
            }
        }
        mTakingGLSurface = false;
        listener.onDispatchDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0.0f, 0.0f, (Paint) null);
        }
        mBitmap = null;
    }

    public static void saveAsPNGIntoSDCard(int x, int y, int w, int h, String name, GL10 gl) {
        Bitmap bmp = savePixels(x, y, w, h, gl);
        try {
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + name);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getPixelsInBitmap(int x, int y, int w, int h, GL10 gl) {
        return savePixels(x, y, w, h, gl);
    }

    private static Bitmap savePixels(int x, int y, int w, int h, GL10 gl) {
        int[] b = new int[(w * h)];
        int[] bt = new int[(w * h)];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, y, w, h, 6408, 5121, ib);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pix = b[(i * w) + j];
                bt[(((h - i) - 1) * w) + j] = (-16711936 & pix) | ((pix << 16) & 16711680) | ((pix >> 16) & MotionEventCompat.ACTION_MASK);
            }
        }
        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.RGB_565);
    }
}
