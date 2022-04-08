package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import android.graphics.Bitmap;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.co.cybird.barcodekanojoForGAM.live2d.util.UIImage;
import jp.live2d.android.UtOpenGL;
//import jp.live2d.base.LDAffineTransform;
import jp.live2d.util.UtDebug;

public class KanojoPartsItemTexture {
    static int[] TEX_SIZE_LIST = {64, 128, 256, 512, 1024, 2048};
	/*LDAffineTransform*/ jp.live2d.base.d clippedToMargedTex;
    ColorConvert colorConvert;
    int glTextureNo;
    int height;
    UIImage image;
	/*LDAffineTransform*/ jp.live2d.base.d localToClippedTex;
    int modelTextureNo;
    transient Bitmap preTextureBitmap;
    int texHeight;
    int texWidth;
    int width;

    KanojoPartsItemTexture(UIImage img) {
        setImage(img);
    }

	void releaseTexture() {
        if (this.preTextureBitmap != null) {
            this.preTextureBitmap.recycle();
            this.preTextureBitmap = null;
        }
        if (this.image != null) {
            this.image.clear();
            this.image = null;
        }
    }

    private void setImage(UIImage img) {
        this.image = img;
        if (img != null) {
            this.width = img.getWidth();
            this.height = img.getHeight();
        }
    }

    void bindTexture_process1(KanojoSetting setting) {
        int texSizeW = -1;
        int texSizeH = -1;
        int i = 0;
        while (true) {
            if (i >= 8) {
                break;
            } else if (this.width <= TEX_SIZE_LIST[i]) {
                texSizeW = TEX_SIZE_LIST[i];
                break;
            } else {
                i++;
            }
        }
        int i2 = 0;
        while (true) {
            if (i2 >= 8) {
                break;
            } else if (this.height <= TEX_SIZE_LIST[i2]) {
                texSizeH = TEX_SIZE_LIST[i2];
                break;
            } else {
                i2++;
            }
        }
        if (texSizeW >= 0 && texSizeH >= 0) {
            this.texWidth = texSizeW;
            this.texHeight = texSizeH;
            Bitmap bitmap1 = this.image.getBitmap();
            int[] imageDataInt = new int[(texSizeW * texSizeH)];
            bitmap1.getPixels(imageDataInt, 0, texSizeW, 0, 0, this.width, this.height);
            if (setting.isSilhouetteMode()) {
                ColorConvertUtil.convertGray(imageDataInt, 128, this.width, this.height, texSizeW, texSizeH);
            } else if (this.colorConvert != null) {
                ColorConvertUtil.convertColor_exe1(imageDataInt, this.colorConvert, this.width, this.height, texSizeW, texSizeH);
            }
            bitmap1.recycle();
            if (this.image != null) {
                this.image.clear();
            }
            this.preTextureBitmap = Bitmap.createBitmap(texSizeW, texSizeH, Bitmap.Config.ARGB_8888);
            this.preTextureBitmap.setPixels(imageDataInt, 0, texSizeW, 0, 0, texSizeW, texSizeH);
        }
    }

    int bindTexture_process2(GL10 gl) {
        if (this.preTextureBitmap == null) {
            return 0;
        }
        try {
            this.glTextureNo = UtOpenGL.buildMipmap(gl, this.preTextureBitmap, false);
        } catch (OutOfMemoryError e) {
            System.gc();
            this.glTextureNo = UtOpenGL.buildMipmap(gl, this.preTextureBitmap, false);
        }
        int error = gl.glGetError();
        if (error != 0) {
            UtDebug.error("load texture error . %d  @KanojoPartsItemTexture", error);
        }
        return this.glTextureNo;
    }

    public UIImage getImage() {
        return this.image;
    }

    public int getGlTextureNo() {
        return this.glTextureNo;
    }

    void setModelTextureNo(int modelTexNo) {
        this.modelTextureNo = modelTexNo;
    }

    int getModelTextureNo() {
        return this.modelTextureNo;
    }

    int getTextureWidth() {
        return this.texWidth;
    }

    int getTextureHeight() {
        return this.texHeight;
    }

    void setColorConvert(ColorConvert conv) {
        this.colorConvert = conv;
    }
}
