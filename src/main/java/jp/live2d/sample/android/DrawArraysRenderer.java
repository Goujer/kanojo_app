package jp.live2d.sample.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.util.UtDebug;

public class DrawArraysRenderer implements GLSurfaceView.Renderer {
    private int CANVAS_W = 200;
    private Context context;
    private DrawArrays drawArrays;
    Live2DModelAndroid model;

    public DrawArraysRenderer(Context context2) {
        this.context = context2;
        this.drawArrays = new DrawArrays();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f, 1.0f, 0.3f, 1.0f);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0.0f, 1.0f, 0.0f, 1.0f);
        try {
            this.model = Live2DModelAndroid.loadModel(this.context.getAssets().open("hibiki/hibiki.moc"));
            System.out.printf("model : %s\t\t\t\t\t@DrawArraysRenderer\n", new Object[]{new StringBuilder().append(this.model).toString()});
            this.model.setTexture(0, loadTexture(gl, "hibiki/hibiki.512/texture_00.png"));
            this.model.setTexture(1, loadTexture(gl, "hibiki/hibiki.512/texture_01.png"));
            this.model.setTexture(2, loadTexture(gl, "hibiki/hibiki.512/texture_02.png"));
            this.model.setTexture(3, loadTexture(gl, "hibiki/hibiki.512/texture_03.png"));
            this.model.setTexture(4, loadTexture(gl, "hibiki/hibiki.512/texture_04.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDrawFrame(GL10 gl) {
        this.drawArrays.draw(gl);
    }

    private class DrawArrays {
        FloatBuffer color;
        FloatBuffer square;
        FloatBuffer texCoord;

        public DrawArrays() {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(32);
            byteBuffer.order(ByteOrder.nativeOrder());
            this.square = byteBuffer.asFloatBuffer();
            this.square.put(new float[]{-400.0f, -400.0f, 400.0f, -400.0f, -400.0f, 400.0f, 400.0f, 400.0f});
            this.square.position(0);
            ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(64);
            byteBuffer2.order(ByteOrder.nativeOrder());
            this.color = byteBuffer2.asFloatBuffer();
            this.color.put(new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f});
            this.color.position(0);
            ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(32);
            byteBuffer3.order(ByteOrder.nativeOrder());
            this.texCoord = byteBuffer3.asFloatBuffer();
            this.texCoord.put(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f});
            this.texCoord.position(0);
        }

        public void draw(GL10 gl) {
            gl.glBlendFunc(1, 771);
            gl.glEnable(3553);
            gl.glEnable(3042);
            gl.glClear(16640);
            gl.glVertexPointer(2, 5126, 0, this.square);
            gl.glEnableClientState(32884);
            gl.glTexCoordPointer(2, 5126, 0, this.texCoord);
            gl.glEnableClientState(32888);
            gl.glLoadIdentity();
            gl.glTranslatef(0.0f, 0.0f, 0.0f);
            gl.glScalef(0.5f, 0.5f, 0.5f);
            try {
                double t = ((double) System.currentTimeMillis()) / 1000.0d;
                DrawArraysRenderer.this.model.setParamFloat("PARAM_ANGLE_X", (float) (Math.sin(t) * -30.0d));
                DrawArraysRenderer.this.model.setParamFloat("PARAM_ANGLE_Y", (float) (Math.sin(1.176d * t) * -30.0d));
                DrawArraysRenderer.this.model.setParamFloat("PARAM_ANGLE_Z", (float) (Math.sin(1.347d * t) * -30.0d));
                DrawArraysRenderer.this.model.setGL(gl);
                UtDebug.start("update");
                DrawArraysRenderer.this.model.update();
                UtDebug.dump("update");
                UtDebug.start("draw");
                DrawArraysRenderer.this.model.draw();
                UtDebug.dump("draw");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glOrthof(0.0f, (float) this.CANVAS_W, (float) ((this.CANVAS_W * height) / width), 0.0f, -100.0f, 100.0f);
        gl.glMatrixMode(5888);
    }

    private int loadTexture(GL10 gl, String path) throws IOException {
        InputStream in = this.context.getAssets().open(path);
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        int textureBufferID = textures[0];
        gl.glBindTexture(3553, textureBufferID);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexEnvf(8960, 8704, 7681.0f);
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        return textureBufferID;
    }

    /* access modifiers changed from: package-private */
    public Bitmap getRawResource(int id) {
        return BitmapFactory.decodeResource(this.context.getResources(), id);
    }
}
