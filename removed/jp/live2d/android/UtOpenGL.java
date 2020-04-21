package jp.live2d.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import jp.live2d.util.UtDebug;

public class UtOpenGL {
    static final int GEN_TEX_LOOP = 999;
    static ByteBuffer colorBuffer;
    static ShortBuffer drawImageBufferIndex = null;
    static FloatBuffer drawImageBufferUv = null;
    static FloatBuffer drawImageBufferVer = null;
    static FloatBuffer drawRectBuffer;
    static int gcCount = 0;
    static byte[] squareColors = new byte[16];
    static float[] squareVertices = new float[8];

    public static int genTexture(GL10 gl) {
        int texture = 0;
        int i = 0;
        while (i < GEN_TEX_LOOP) {
            int[] ret = new int[1];
            gl.glGenTextures(1, ret, 0);
            texture = ret[0];
            if (texture >= 0) {
                break;
            }
            gl.glDeleteTextures(1, ret, 0);
            i++;
        }
        if (i != GEN_TEX_LOOP) {
            return texture;
        }
        UtDebug.error("gen texture loops over 999times @UtOpenGL", new Object[0]);
        return 0;
    }

    public static FloatBuffer createFloatBuffer(int floatCount) {
        ByteBuffer data = ByteBuffer.allocateDirect(floatCount * 4);
        data.order(ByteOrder.nativeOrder());
        return data.asFloatBuffer();
    }

    public static FloatBuffer setupFloatBuffer(FloatBuffer preBuffer, float[] array) {
        if (preBuffer == null || preBuffer.capacity() < array.length) {
            preBuffer = createFloatBuffer(array.length * 2);
        } else {
            preBuffer.clear();
        }
        preBuffer.put(array);
        preBuffer.position(0);
        return preBuffer;
    }

    public static ShortBuffer createShortBuffer(int shortCount) {
        ByteBuffer data = ByteBuffer.allocateDirect(shortCount * 4);
        data.order(ByteOrder.nativeOrder());
        return data.asShortBuffer();
    }

    private static ShortBuffer setupShortBuffer(ShortBuffer preBuffer, short[] array) {
        if (preBuffer == null || preBuffer.capacity() < array.length) {
            preBuffer = createShortBuffer(array.length * 2);
        } else {
            preBuffer.clear();
        }
        preBuffer.clear();
        preBuffer.put(array);
        preBuffer.position(0);
        return preBuffer;
    }

    public static ByteBuffer createByteBuffer(int count) {
        ByteBuffer data = ByteBuffer.allocateDirect(count * 4);
        data.order(ByteOrder.nativeOrder());
        return data;
    }

    public static ByteBuffer setupByteBuffer(ByteBuffer preBuffer, byte[] array) {
        if (preBuffer == null || preBuffer.capacity() < array.length) {
            preBuffer = createByteBuffer(array.length * 2);
        } else {
            preBuffer.clear();
        }
        preBuffer.put(array);
        preBuffer.position(0);
        return preBuffer;
    }

    public static void drawImage(GL10 gl, int img, float x, float y, float w, float h) {
        drawImage(gl, img, x, y, w, h, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public static void drawImage(GL10 gl, int img, float x, float y, float w, float h, float uvx, float uvy, float uvw, float uvh) {
        short[] index = new short[6];
        index[1] = 1;
        index[2] = 2;
        index[4] = 2;
        index[5] = 3;
        drawImageBufferUv = setupFloatBuffer(drawImageBufferUv, new float[]{uvx, uvy, uvx + uvw, uvy, uvx + uvw, uvy + uvh, uvx, uvy + uvh});
        drawImageBufferVer = setupFloatBuffer(drawImageBufferVer, new float[]{x, y, x + w, y, x + w, y + h, x, y + h});
        drawImageBufferIndex = setupShortBuffer(drawImageBufferIndex, index);
        gl.glTexCoordPointer(2, 5126, 0, drawImageBufferUv);
        gl.glVertexPointer(2, 5126, 0, drawImageBufferVer);
        gl.glBindTexture(3553, img);
        gl.glDrawElements(4, 6, 5123, drawImageBufferIndex);
    }

    public static int loadTexture(GL10 gl, Context context, String path, boolean mipmap) throws IOException {
        return loadTexture(gl, context.getAssets().open(path), mipmap);
    }

    public static int loadTexture(GL10 gl, InputStream in, boolean mipmap) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        if (mipmap) {
            return buildMipmap(gl, bitmap);
        }
        int textureID = genTexture(gl);
        gl.glBindTexture(3553, textureID);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexEnvf(8960, 8704, 8448.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        return textureID;
    }

    public static int buildMipmap(GL10 gl, Bitmap bitmap) {
        return buildMipmap(gl, bitmap, true);
    }

    public static int buildMipmap(GL10 gl, Bitmap srcBitmap, boolean recycle) {
        Bitmap bitmap = srcBitmap;
        int level = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int textureID = genTexture(gl);
        gl.glBindTexture(3553, textureID);
        try {
            ((GL11) gl).glTexParameteri(3553, 33169, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10241, 9987.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexEnvf(8960, 8704, 8448.0f);
        while (true) {
            if (height < 1 || width < 1) {
                break;
            }
            GLUtils.texImage2D(3553, level, bitmap, 0);
            if (height != 1 && width != 1) {
                level++;
                height /= 2;
                width /= 2;
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
                if (recycle || bitmap != srcBitmap) {
                    bitmap.recycle();
                }
                bitmap = bitmap2;
            } else if (recycle || bitmap != srcBitmap) {
                bitmap.recycle();
            }
        }
        return textureID;
    }

    public static void drawClippedRect(GL10 gl, float x, float y, float w, float h, float clipx, float clipy, float clipw, float cliph, int color) {
        drawRect(gl, x, y, clipx - x, h, color);
        GL10 gl10 = gl;
        drawRect(gl10, clipx + clipw, y, (x + w) - (clipx + clipw), h, color);
        drawRect(gl, clipx, y, clipw, clipy - y, color);
        GL10 gl102 = gl;
        float f = clipx;
        drawRect(gl102, f, clipy + cliph, clipw, (y + h) - (clipy + cliph), color);
    }

    public static void drawRect(GL10 gl, float x, float y, float w, float h, int argb) {
        if (w != 0.0f && h != 0.0f) {
            int alpha = (argb >> 24) & MotionEventCompat.ACTION_MASK;
            int red = (argb >> 16) & MotionEventCompat.ACTION_MASK;
            int green = (argb >> 8) & MotionEventCompat.ACTION_MASK;
            int blue = argb & MotionEventCompat.ACTION_MASK;
            squareVertices[0] = x;
            squareVertices[1] = y;
            squareVertices[2] = x + w;
            squareVertices[3] = y;
            squareVertices[4] = x;
            squareVertices[5] = y + h;
            squareVertices[6] = x + w;
            squareVertices[7] = y + h;
            squareColors[0] = (byte) red;
            squareColors[4] = (byte) red;
            squareColors[8] = (byte) red;
            squareColors[12] = (byte) red;
            squareColors[1] = (byte) green;
            squareColors[5] = (byte) green;
            squareColors[9] = (byte) green;
            squareColors[13] = (byte) green;
            squareColors[2] = (byte) blue;
            squareColors[6] = (byte) blue;
            squareColors[10] = (byte) blue;
            squareColors[14] = (byte) blue;
            squareColors[3] = (byte) alpha;
            squareColors[7] = (byte) alpha;
            squareColors[11] = (byte) alpha;
            squareColors[15] = (byte) alpha;
            drawRectBuffer = setupFloatBuffer(drawRectBuffer, squareVertices);
            colorBuffer = setupByteBuffer(colorBuffer, squareColors);
            gl.glDisable(3553);
            gl.glEnableClientState(32884);
            gl.glEnableClientState(32886);
            gl.glVertexPointer(2, 5126, 0, drawRectBuffer);
            gl.glColorPointer(4, 5121, 0, colorBuffer);
            gl.glDrawArrays(5, 0, 4);
            gl.glDisableClientState(32886);
            gl.glDisableClientState(32884);
        }
    }
}
