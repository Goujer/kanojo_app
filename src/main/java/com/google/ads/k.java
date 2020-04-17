package com.google.ads;

import com.google.ads.AdRequest;
import com.google.ads.g;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.util.a;
import com.google.ads.util.b;

class k implements MediationInterstitialListener {
    private final h a;

    k(h hVar) {
        this.a = hVar;
    }

    public void onReceivedAd(MediationInterstitialAdapter<?, ?> adapter) {
        synchronized (this.a) {
            a.a((Object) adapter, (Object) this.a.i());
            if (this.a.c()) {
                b.e("Got an onReceivedAd() callback after loadAdTask is done from an interstitial adapter. Ignoring callback.");
            } else {
                this.a.a(true, g.a.AD);
            }
        }
    }

    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> adapter, AdRequest.ErrorCode error) {
        synchronized (this.a) {
            a.a((Object) adapter, (Object) this.a.i());
            b.a("Mediation adapter " + adapter.getClass().getName() + " failed to receive ad with error code: " + error);
            if (this.a.c()) {
                b.b("Got an onFailedToReceiveAd() callback after loadAdTask is done from an interstitial adapter.  Ignoring callback.");
            } else {
                this.a.a(false, error == AdRequest.ErrorCode.NO_FILL ? g.a.NO_FILL : g.a.ERROR);
            }
        }
    }

    public void onPresentScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        synchronized (this.a) {
            this.a.j().a(this.a);
        }
    }

    public void onDismissScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        synchronized (this.a) {
            this.a.j().b(this.a);
        }
    }

    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        synchronized (this.a) {
            this.a.j().c(this.a);
        }
    }
}
