package com.google.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import com.google.ads.internal.ActivationOverlay;
import com.google.ads.internal.d;
import com.google.ads.internal.h;
import com.google.ads.util.i;

public class n extends i {
    public final i.b<Ad> a;
    public final i.b<d> b;
    public final i.d<Activity> c;
    public final i.b<m> d;
    public final i.b<ActivationOverlay> e;
    public final i.b<Context> f;
    public final i.b<h> g;
    public final i.b<String> h;
    public final i.b<ViewGroup> i;
    public final i.b<AdView> j;
    public final i.b<InterstitialAd> k;
    public final i.c<l> l = new i.c<>("currentAd", null);
    public final i.c<l> m = new i.c<>("nextAd", null);
    public final i.c<AdSize[]> n;
    public final i.c<AdListener> o = new i.c<>("adListener");
    public final i.c<AppEventListener> p = new i.c<>("appEventListener");
    public final i.c<SwipeableAdListener> q = new i.c<>("swipeableEventListener");
    public final i.c<ak> r = new i.c<>("spamSignals", null);
    public final i.c<al> s = new i.c<>("spamSignalsUtil", null);
    public final i.c<Boolean> t = new i.c<>("usesManualImpressions", false);

    public boolean a() {
        return !b();
    }

    public boolean b() {
        return this.g.a().a();
    }

    public n(m mVar, Ad ad, AdView adView, InterstitialAd interstitialAd, String str, Activity activity, Context context, ViewGroup viewGroup, h hVar, d dVar) {
        ActivationOverlay activationOverlay = null;
        this.d = new i.b<>("appState", mVar);
        this.a = new i.b<>("ad", ad);
        this.j = new i.b<>("adView", adView);
        this.g = new i.b<>("adType", hVar);
        this.h = new i.b<>("adUnitId", str);
        this.c = new i.d<>("activity", activity);
        this.k = new i.b<>("interstitialAd", interstitialAd);
        this.i = new i.b<>("bannerContainer", viewGroup);
        this.f = new i.b<>("applicationContext", context);
        this.n = new i.c<>("adSizes", null);
        this.b = new i.b<>("adManager", dVar);
        if (hVar != null && hVar.b()) {
            activationOverlay = new ActivationOverlay(this);
        }
        this.e = new i.b<>("activationOverlay", activationOverlay);
    }
}
