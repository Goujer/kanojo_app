package jp.live2d.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import jp.live2d.graphics.DrawParam;

public class DrawParam_Android extends DrawParam {
    static final int DEFAULT_PICTURE_ARRAY = 32;
    static final int DUMMY_GL_TEXTURE_NO = 9999;
    static ShortBuffer indexBuffer;
    static FloatBuffer uvBuffer;
    static FloatBuffer vertexBuffer;
    GL10 gl;
    private int[] textures = new int[32];

    public DrawParam_Android() {
        uvBuffer = createFloatBuffer(256);
        vertexBuffer = createFloatBuffer(256);
        indexBuffer = createShortBuffer(256);
    }

    public void setGL(GL10 gl2) {
        this.gl = gl2;
    }

    private static FloatBuffer createFloatBuffer(int floatCount) {
        ByteBuffer data = ByteBuffer.allocateDirect(floatCount * 4);
        data.order(ByteOrder.nativeOrder());
        return data.asFloatBuffer();
    }

    private static ShortBuffer createShortBuffer(int shortCount) {
        return ShortBuffer.allocate(shortCount);
    }

    private static FloatBuffer setupFloatBuffer(FloatBuffer preBuffer, float[] array) {
        if (preBuffer == null || preBuffer.capacity() < array.length) {
            FloatBuffer preBuffer2 = createFloatBuffer(array.length * 2);
            preBuffer2.put(array);
            preBuffer2.position(0);
            return preBuffer2;
        }
        preBuffer.clear();
        preBuffer.put(array);
        preBuffer.position(0);
        return preBuffer;
    }

    private static ShortBuffer setupShortBuffer(ShortBuffer preBuffer, short[] array) {
        if (preBuffer == null || preBuffer.capacity() < array.length) {
            ShortBuffer preBuffer2 = createShortBuffer(array.length * 2);
            preBuffer2.put(array);
            preBuffer2.position(0);
            return preBuffer2;
        }
        preBuffer.clear();
        preBuffer.put(array);
        preBuffer.position(0);
        return preBuffer;
    }

    public void drawTexture(int textureNo, int vertexCount, short[] indexArray, float[] vertexArray, float[] uvArray, float opacity) {
        if (((double) opacity) >= 0.01d) {
            GL10 gl2 = this.gl;
            if (gl2 == null) {
                throw new RuntimeException("描画の前にmodel.setGL( gl )を呼ぶ必要があります");
            }
            gl2.glEnable(3553);
            gl2.glEnable(3042);
            gl2.glBlendFunc(1, 771);
            gl2.glColor4f(opacity, opacity, opacity, opacity);
            gl2.glBindTexture(3553, this.textures[textureNo]);
            gl2.glEnableClientState(32888);
            gl2.glEnableClientState(32884);
            uvBuffer = setupFloatBuffer(uvBuffer, uvArray);
            gl2.glTexCoordPointer(2, 5126, 0, uvBuffer);
            vertexBuffer = setupFloatBuffer(vertexBuffer, vertexArray);
            gl2.glVertexPointer(2, 5126, 0, vertexBuffer);
            indexBuffer = setupShortBuffer(indexBuffer, indexArray);
            gl2.glDrawElements(4, vertexCount, 5123, indexBuffer);
        }
    }

    public int generateModelTextureNo() {
        int len = this.textures.length;
        for (int i = this.fixedTexureCount; i < len; i++) {
            if (this.textures[i] < 1) {
                this.textures[i] = DUMMY_GL_TEXTURE_NO;
                return i;
            }
        }
        int[] tmp = new int[(len * 2)];
        System.arraycopy(this.textures, 0, tmp, 0, this.textures.length);
        this.textures = tmp;
        this.textures[len] = DUMMY_GL_TEXTURE_NO;
        return len;
    }

    public void releaseModelTextureNo(int no) {
        if (no < this.textures.length) {
            this.textures[no] = 0;
        }
    }

    public void setTexture(int modelTextureNo, int texNo) {
        int len = this.textures.length;
        if (len < modelTextureNo + 1) {
            int[] tmp = new int[Math.max(len * 2, modelTextureNo + 1 + 10)];
            System.arraycopy(this.textures, 0, tmp, 0, this.textures.length);
            this.textures = tmp;
        }
        this.textures[modelTextureNo] = texNo;
    }
}
