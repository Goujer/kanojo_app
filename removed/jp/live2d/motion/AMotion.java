package jp.live2d.motion;

import jp.live2d.ALive2DModel;
import jp.live2d.motion.MotionQueueManager;
import jp.live2d.util.UtMotion;
import jp.live2d.util.UtSystem;

public abstract class AMotion {
    int fadeInMsec = 1000;
    int fadeOutMsec = 1000;
    float weight = 1.0f;

    public abstract void updateParamExe(ALive2DModel aLive2DModel, long j, float f, MotionQueueManager.MotionQueueEnt motionQueueEnt);

    public AMotion() {
        reinit();
    }

    public void reinit() {
    }

    public void setFadeIn(int fadeInMsec2) {
        this.fadeInMsec = fadeInMsec2;
    }

    public void setFadeOut(int fadeOutMsec2) {
        this.fadeOutMsec = fadeOutMsec2;
    }

    public void setWeight(float weight2) {
        this.weight = weight2;
    }

    public int getFadeOut() {
        return this.fadeOutMsec;
    }

    public int getFadeIn() {
        return this.fadeOutMsec;
    }

    public float getWeight() {
        return this.weight;
    }

    public int getDurationMSec() {
        return -1;
    }

    public static float getEasing(float time, float totalTime, float accelerateTime) {
        float t_ = time / totalTime;
        float in = accelerateTime / totalTime;
        float out = in;
        float inRate = 1.0f - ((1.0f - in) * (1.0f - in));
        float outRate = 1.0f - ((1.0f - out) * (1.0f - out));
        float y2 = ((1.0f - in) * 0.33333334f * inRate) + (((out * 0.6666667f) + ((1.0f - out) * 0.33333334f)) * (1.0f - inRate));
        float y3 = ((((1.0f - out) * 0.6666667f) + out) * outRate) + (((in * 0.33333334f) + ((1.0f - in) * 0.6666667f)) * (1.0f - outRate));
        float a = ((1.0f - (3.0f * y3)) + (3.0f * y2)) - 0.0f;
        float b = ((3.0f * y3) - (6.0f * y2)) + (3.0f * 0.0f);
        float c = (3.0f * y2) - (3.0f * 0.0f);
        if (t_ <= 0.0f) {
            return 0.0f;
        }
        if (t_ >= 1.0f) {
            return 1.0f;
        }
        float s = t_;
        float s2 = s * s;
        return (a * s * s2) + (b * s2) + (c * s) + 0.0f;
    }

    public void updateParam(ALive2DModel model, MotionQueueManager.MotionQueueEnt motionQueueEnt) {
        long j;
        if (motionQueueEnt.available && !motionQueueEnt.finished) {
            long timeMSec = UtSystem.getUserTimeMSec();
            if (motionQueueEnt.startTimeMSec < 0) {
                motionQueueEnt.startTimeMSec = timeMSec;
                motionQueueEnt.fadeInStartTimeMSec = timeMSec;
                int duration = getDurationMSec();
                if (duration <= 0) {
                    j = -1;
                } else {
                    j = motionQueueEnt.startTimeMSec + ((long) duration);
                }
                motionQueueEnt.endTimeMSec = j;
            }
            updateParamExe(model, timeMSec, this.weight * (this.fadeInMsec == 0 ? 1.0f : UtMotion.getEasingSine(((float) (timeMSec - motionQueueEnt.fadeInStartTimeMSec)) / ((float) this.fadeInMsec))) * ((this.fadeOutMsec == 0 || motionQueueEnt.endTimeMSec < 0) ? 1.0f : UtMotion.getEasingSine(((float) (motionQueueEnt.endTimeMSec - timeMSec)) / ((float) this.fadeOutMsec))), motionQueueEnt);
            if (motionQueueEnt.endTimeMSec > 0 && motionQueueEnt.endTimeMSec < timeMSec) {
                motionQueueEnt.finished = true;
            }
        }
    }
}
