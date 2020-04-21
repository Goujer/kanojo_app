package jp.live2d.motion;

import jp.co.cybird.app.android.lib.commons.file.json.JSONException;
import jp.co.cybird.barcodekanojoForGAM.live2d.motion.KanojoAnimation;
import jp.live2d.ALive2DModel;
import jp.live2d.util.UtSystem;

public class EyeBlinkMotion {
    private static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$motion$EyeBlinkMotion$EYE_STATE;
    int blinkIntervalMsec = KanojoAnimation.EYE_INTERVAL_NORMAL;
    boolean closeIfZero = true;
    int closedMotionMsec = 50;
    int closingMotionMsec = 100;
    String eyeID_L = "PARAM_EYE_L_OPEN";
    String eyeID_R = "PARAM_EYE_R_OPEN";
    EYE_STATE eyeState = EYE_STATE.STATE_FIRST;
    long nextBlinkTime;
    int openingMotionMsec = JSONException.PREFORMAT_ERROR;
    long stateStartTime;

    enum EYE_STATE {
        STATE_FIRST,
        STATE_INTERVAL,
        STATE_CLOSING,
        STATE_CLOSED,
        STATE_OPENING
    }

    static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$motion$EyeBlinkMotion$EYE_STATE() {
        int[] iArr = $SWITCH_TABLE$jp$live2d$motion$EyeBlinkMotion$EYE_STATE;
        if (iArr == null) {
            iArr = new int[EYE_STATE.values().length];
            try {
                iArr[EYE_STATE.STATE_CLOSED.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EYE_STATE.STATE_CLOSING.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EYE_STATE.STATE_FIRST.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EYE_STATE.STATE_INTERVAL.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EYE_STATE.STATE_OPENING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$jp$live2d$motion$EyeBlinkMotion$EYE_STATE = iArr;
        }
        return iArr;
    }

    public long calcNextBlink() {
        return (long) (((double) UtSystem.getUserTimeMSec()) + (((double) ((this.blinkIntervalMsec * 2) - 1)) * Math.random()));
    }

    public void setInterval(int blinkIntervalMsec2) {
        this.blinkIntervalMsec = blinkIntervalMsec2;
    }

    public void setEyeMotion(int closingMotionMsec2, int closedMotionMsec2, int openingMotionMsec2) {
        this.closingMotionMsec = closingMotionMsec2;
        this.closedMotionMsec = closedMotionMsec2;
        this.openingMotionMsec = openingMotionMsec2;
    }

    public void setParam(ALive2DModel model) {
        float eyeParamValue;
        long time = UtSystem.getUserTimeMSec();
        switch ($SWITCH_TABLE$jp$live2d$motion$EyeBlinkMotion$EYE_STATE()[this.eyeState.ordinal()]) {
            case 2:
                if (this.nextBlinkTime < time) {
                    this.eyeState = EYE_STATE.STATE_CLOSING;
                    this.stateStartTime = time;
                }
                eyeParamValue = 1.0f;
                break;
            case 3:
                float t = ((float) (time - this.stateStartTime)) / ((float) this.closingMotionMsec);
                if (t >= 1.0f) {
                    t = 1.0f;
                    this.eyeState = EYE_STATE.STATE_CLOSED;
                    this.stateStartTime = time;
                }
                eyeParamValue = 1.0f - t;
                break;
            case 4:
                if (((float) (time - this.stateStartTime)) / ((float) this.closedMotionMsec) >= 1.0f) {
                    this.eyeState = EYE_STATE.STATE_OPENING;
                    this.stateStartTime = time;
                }
                eyeParamValue = 0.0f;
                break;
            case 5:
                float t2 = ((float) (time - this.stateStartTime)) / ((float) this.openingMotionMsec);
                if (t2 >= 1.0f) {
                    t2 = 1.0f;
                    this.eyeState = EYE_STATE.STATE_INTERVAL;
                    this.nextBlinkTime = calcNextBlink();
                }
                eyeParamValue = t2;
                break;
            default:
                this.eyeState = EYE_STATE.STATE_INTERVAL;
                this.nextBlinkTime = calcNextBlink();
                eyeParamValue = 1.0f;
                break;
        }
        if (!this.closeIfZero) {
            eyeParamValue = -eyeParamValue;
        }
        model.setParamFloat(this.eyeID_L, eyeParamValue);
        model.setParamFloat(this.eyeID_R, eyeParamValue);
    }
}
