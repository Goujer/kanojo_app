package com.google.ads.mediation;

import com.google.ads.AdRequest;

public interface MediationBannerListener {
    void onClick(MediationBannerAdapter<?, ?> mediationBannerAdapter);

    void onDismissScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter);

    void onFailedToReceiveAd(MediationBannerAdapter<?, ?> mediationBannerAdapter, AdRequest.ErrorCode errorCode);

    void onLeaveApplication(MediationBannerAdapter<?, ?> mediationBannerAdapter);

    void onPresentScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter);

    void onReceivedAd(MediationBannerAdapter<?, ?> mediationBannerAdapter);
}
