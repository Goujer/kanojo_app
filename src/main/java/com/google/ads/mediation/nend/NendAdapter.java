package com.google.ads.mediation.nend;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import jp.co.cybird.app.android.lib.commons.file.json.JSONException;
import net.nend.android.NendAdListener;
import net.nend.android.NendAdView;

public final class NendAdapter implements MediationBannerAdapter<NendAdapterExtras, NendAdapterServerParameters>, MediationInterstitialAdapter<NendAdapterExtras, NendAdapterServerParameters>, NendAdListener {
    public static final String VERSION = "1.1.0";
    private MediationBannerListener mListener;
    private NendAdView mNendAdView;

    public Class<NendAdapterExtras> getAdditionalParametersType() {
        return NendAdapterExtras.class;
    }

    public Class<NendAdapterServerParameters> getServerParametersType() {
        return NendAdapterServerParameters.class;
    }

    public void requestBannerAd(MediationBannerListener listener, Activity activity, NendAdapterServerParameters serverParameters, AdSize adSize, MediationAdRequest adRequest, NendAdapterExtras extras) {
        if (adSize.isSizeAppropriate(320, 50) || adSize.isSizeAppropriate(300, JSONException.POSTPARSE_ERROR) || adSize.isSizeAppropriate(728, 90)) {
            this.mListener = listener;
            this.mNendAdView = new NendAdView((Context) activity, Integer.parseInt(serverParameters.spotIdStr), serverParameters.apiKey);
            this.mNendAdView.pause();
            this.mNendAdView.setListener(this);
            this.mNendAdView.loadAd();
            return;
        }
        listener.onFailedToReceiveAd(this, AdRequest.ErrorCode.NO_FILL);
    }

    public View getBannerView() {
        return this.mNendAdView;
    }

    public void destroy() {
        this.mNendAdView = null;
        this.mListener = null;
    }

    public void onReceiveAd(NendAdView adView) {
        if (this.mListener != null) {
            this.mListener.onReceivedAd(this);
        }
    }

    public void onFailedToReceiveAd(NendAdView adView) {
        if (this.mListener != null) {
            this.mListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
        }
    }

    public void onClick(NendAdView adView) {
        if (this.mListener != null) {
            this.mListener.onClick(this);
            this.mListener.onPresentScreen(this);
            this.mListener.onLeaveApplication(this);
        }
    }

    public void onDismissScreen(NendAdView adView) {
        if (this.mListener != null) {
            this.mListener.onDismissScreen(this);
        }
    }

    public void requestInterstitialAd(MediationInterstitialListener listener, Activity activity, NendAdapterServerParameters serverParameters, MediationAdRequest adRequest, NendAdapterExtras extras) {
        listener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INVALID_REQUEST);
    }

    public void showInterstitial() {
        throw new UnsupportedOperationException();
    }
}
