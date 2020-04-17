package android.support.v4.view;

import android.animation.ValueAnimator;

class ViewCompatHC {
    ViewCompatHC() {
    }

    static long getFrameTime() {
        return ValueAnimator.getFrameDelay();
    }
}
