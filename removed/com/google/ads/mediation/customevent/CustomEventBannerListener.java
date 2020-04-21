package com.google.ads.mediation.customevent;

import android.view.View;

public interface CustomEventBannerListener extends CustomEventListener {
    void onClick();

    void onReceivedAd(View view);
}
