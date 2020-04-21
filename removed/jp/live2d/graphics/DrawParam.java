package jp.live2d.graphics;

import jp.live2d.Live2D;

public class DrawParam {
    public static final int DEFAULT_FIXED_TEXTURE_COUNT = 32;
    protected int fixedTexureCount = 32;

    public void drawTexture(int textureNo, int vertexCount, short[] indexArray, float[] vertexArray, float[] uvArray, float opacity) {
    }

    public int generateModelTextureNo() {
        if (!Live2D.L2D_DEBUG) {
            return -1;
        }
        System.out.printf("オーバーライドする必要があります @generateModelTextureNo()", new Object[0]);
        return -1;
    }

    public void releaseModelTextureNo(int no) {
        if (Live2D.L2D_DEBUG) {
            System.out.printf("オーバーライドする必要があります @releaseModelTextureNo()", new Object[0]);
        }
    }
}
