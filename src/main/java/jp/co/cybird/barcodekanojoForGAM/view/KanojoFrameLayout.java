package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import jp.co.cybird.barcodekanojoForGAM.activity.util.KanojoScreenShot;

public class KanojoFrameLayout extends FrameLayout {
    public KanojoFrameLayout(Context context) {
        super(context);
    }

    public KanojoFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takeScreenShot(Handler handler) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        KanojoScreenShot.GLSurface.start();
        dispatchDraw(canvas);
        handler.sendMessage(Message.obtain(handler, 1, bitmap));
    }
}
