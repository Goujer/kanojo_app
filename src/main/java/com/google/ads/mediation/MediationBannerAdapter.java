package com.google.ads.mediation;

import android.app.Activity;
import android.view.View;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;

public interface MediationBannerAdapter<ADDITIONAL_PARAMETERS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends MediationAdapter<ADDITIONAL_PARAMETERS, SERVER_PARAMETERS> {
    View getBannerView();

    void requestBannerAd(MediationBannerListener mediationBannerListener, Activity activity, SERVER_PARAMETERS server_parameters, AdSize adSize, MediationAdRequest mediationAdRequest, ADDITIONAL_PARAMETERS additional_parameters);
}
