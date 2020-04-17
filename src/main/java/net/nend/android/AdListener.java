package net.nend.android;

import net.nend.android.NendAdView;

interface AdListener {
    void onFailedToReceiveAd(NendAdView.NendError nendError);

    void onReceiveAd();
}
