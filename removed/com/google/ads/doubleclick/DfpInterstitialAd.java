package com.google.ads.doubleclick;

import android.app.Activity;
import com.google.ads.AppEventListener;
import com.google.ads.InterstitialAd;

public class DfpInterstitialAd extends InterstitialAd {
    public DfpInterstitialAd(Activity activity, String adUnitId) {
        super(activity, adUnitId);
    }

    public DfpInterstitialAd(Activity activity, String adUnitId, boolean shortTimeout) {
        super(activity, adUnitId, shortTimeout);
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        super.setAppEventListener(appEventListener);
    }
}
