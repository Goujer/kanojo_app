package com.google.ads.doubleclick;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import com.google.ads.AdSize;
import com.google.ads.SwipeableAdListener;

public class SwipeableDfpAdView extends DfpAdView {
    public SwipeableDfpAdView(Activity activity, AdSize adSize, String adUnitID) {
        super(activity, adSize, adUnitID);
    }

    public SwipeableDfpAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeableDfpAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSwipeableEventListener(SwipeableAdListener swipeableAdListener) {
        super.setSwipeableEventListener(swipeableAdListener);
    }

    public void resize(AdSize adSize) {
        super.resize(adSize);
        if (this.a.i().e.a().b()) {
            this.a.a(-1, -1, adSize.getWidth(), adSize.getHeight());
        }
    }
}
