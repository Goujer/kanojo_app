package jp.co.cybird.barcodekanojoForGAM.live2d.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;

public class UIImage {
    Bitmap bitmap;

    private UIImage(Bitmap bi) {
        this.bitmap = bi;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public int getWidth() {
        if (this.bitmap == null) {
            return 0;
        }
        return this.bitmap.getWidth();
    }

    public int getHeight() {
        if (this.bitmap == null) {
            return 0;
        }
        return this.bitmap.getHeight();
    }

    public static UIImage loadImage(InputStream in) {
        return new UIImage(BitmapFactory.decodeStream(in));
    }

    public void clear() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }
}
