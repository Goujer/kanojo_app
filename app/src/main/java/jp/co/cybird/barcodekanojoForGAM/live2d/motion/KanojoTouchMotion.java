package jp.co.cybird.barcodekanojoForGAM.live2d.motion;

import jp.live2d.ALive2DModel;
import jp.live2d.motion.AMotion;
import jp.live2d.motion.MotionQueueManager;
import jp.live2d.type.LDRectF;

public class KanojoTouchMotion extends AMotion {
    private static KanojoTouchMotion clothesPull = setup("PARAM_CLOTHES_Y", 0.0f, -3.3333333f, -1.0f, 0.0f, 0.8f, -0.2f, 0.4f, 0.4f, 0.3f);
    private static KanojoTouchMotion[] entries = {clothesPull};
    private boolean finished = false;
    private float lastX;
    private float lastY;
    private float paramBackScale;
    private String paramID;
    private float paramMax;
    private float paramMin;
    private float paramVX;
    private float paramVY;
    private float startX;
    private float startY;
    private LDRectF touchBounds = new LDRectF();
    private float v = 0.0f;

    private static KanojoTouchMotion setup(String id, float paramVX2, float paramVY2, float paramMin2, float paramMax2, float paramBackScale2, float x, float y, float w, float h) {
        KanojoTouchMotion ret = new KanojoTouchMotion();
        ret.paramID = id;
        ret.paramVX = paramVX2;
        ret.paramVY = paramVY2;
        ret.paramMin = paramMin2;
        ret.paramMax = paramMax2;
        ret.paramBackScale = paramBackScale2;
		ret.touchBounds.setRect(x, y, w, h);
        return ret;
    }

    static KanojoTouchMotion getTouchEntry(float x, float y) {
        System.out.printf("mouse (%6.3f,%6.3f)\t\t\t\t\t@@KanojoAnimation\n", new Object[]{Float.valueOf(x), Float.valueOf(y)});
        int i = 0;
        while (i < entries.length) {
            KanojoTouchMotion e = entries[i];
			if (e.touchBounds.a/*getX()*/ > x || x > e.touchBounds.a/*getX()*/ + e.touchBounds.c/*getWidth()*/ || e.touchBounds.b/*getY()*/ > y || y > e.touchBounds.b/*getY()*/ + e.touchBounds.d/*getHeight()*/) {
                i++;
            } else {
                e.startX = x;
                e.startY = y;
                e.finished = false;
                e.lastX = x;
                e.lastY = y;
                return e;
            }
        }
        return null;
    }

    private KanojoTouchMotion() {
        setFadeIn(300);
        setFadeOut(300);
    }

    void touchesMoved(float mouseX, float mouseY) {
        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    void touchesEnded() {
        this.finished = true;
    }

    public void updateParamExe(ALive2DModel model, long timeMSec, float weight, MotionQueueManager.MotionQueueEnt motionQueueEnt) {
        if (this.finished) {
            this.v *= this.paramBackScale;
            if (-0.04d < ((double) this.v) && ((double) this.v) < 0.04d) {
                motionQueueEnt.startFadeout(0);
            }
        } else {
            this.v = ((this.lastX - this.startX) * this.paramVX) + ((this.lastY - this.startY) * this.paramVY);
        }
        if (this.v < this.paramMin) {
            this.v = this.paramMin;
        } else if (this.v > this.paramMax) {
            this.v = this.paramMax;
        }
        model.setParamFloat(this.paramID, this.v, weight);
    }
}
