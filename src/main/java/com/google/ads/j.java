package com.google.ads;

import com.google.ads.AdRequest;
import com.google.ads.g;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.util.a;
import com.google.ads.util.b;

class j implements MediationBannerListener {
    private final h a;
    private boolean b;

    public j(h hVar) {
        this.a = hVar;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void onReceivedAd(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            a.a((Object) adapter, (Object) this.a.i());
            try {
                this.a.a(adapter.getBannerView());
                if (!this.a.c()) {
                    this.b = false;
                    this.a.a(true, g.a.AD);
                    return;
                }
                this.b = true;
                this.a.j().a(this.a, this.a.f());
            } catch (Throwable th) {
                b.b("Error while getting banner View from adapter (" + this.a.h() + "): ", th);
                if (!this.a.c()) {
                    this.a.a(false, g.a.EXCEPTION);
                }
            }
        }
    }

    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> adapter, AdRequest.ErrorCode error) {
        synchronized (this.a) {
            a.a((Object) adapter, (Object) this.a.i());
            b.a("Mediation adapter " + adapter.getClass().getName() + " failed to receive ad with error code: " + error);
            if (!this.a.c()) {
                this.a.a(false, error == AdRequest.ErrorCode.NO_FILL ? g.a.NO_FILL : g.a.ERROR);
            }
        }
    }

    public void onPresentScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        synchronized (this.a) {
            this.a.j().a(this.a);
        }
    }

    public void onDismissScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        synchronized (this.a) {
            this.a.j().b(this.a);
        }
    }

    public void onLeaveApplication(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        synchronized (this.a) {
            this.a.j().c(this.a);
        }
    }

    public void onClick(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        synchronized (this.a) {
            a.a(this.a.c());
            this.a.j().a(this.a, this.b);
        }
    }
}
