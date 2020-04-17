package net.nend.android;

import java.util.EventListener;

public interface NendAdListener extends EventListener {
    void onClick(NendAdView nendAdView);

    void onDismissScreen(NendAdView nendAdView);

    void onFailedToReceiveAd(NendAdView nendAdView);

    void onReceiveAd(NendAdView nendAdView);
}
