package com.google.ads.mediation.customevent;

import android.app.Activity;
import android.view.View;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.g;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;

public class CustomEventAdapter implements MediationBannerAdapter<CustomEventExtras, CustomEventServerParameters>, MediationInterstitialAdapter<CustomEventExtras, CustomEventServerParameters> {
    /* access modifiers changed from: private */
    public String a;
    private CustomEventBanner b = null;
    private a c = null;
    private CustomEventInterstitial d = null;

    private class a implements CustomEventBannerListener {
        private View b;
        private final MediationBannerListener c;

        public a(MediationBannerListener mediationBannerListener) {
            this.c = mediationBannerListener;
        }

        public synchronized void onReceivedAd(View view) {
            com.google.ads.util.b.a(b() + " called onReceivedAd.");
            this.b = view;
            this.c.onReceivedAd(CustomEventAdapter.this);
        }

        public void onFailedToReceiveAd() {
            com.google.ads.util.b.a(b() + " called onFailedToReceiveAd().");
            this.c.onFailedToReceiveAd(CustomEventAdapter.this, AdRequest.ErrorCode.NO_FILL);
        }

        public void onClick() {
            com.google.ads.util.b.a(b() + " called onClick().");
            this.c.onClick(CustomEventAdapter.this);
        }

        public void onPresentScreen() {
            com.google.ads.util.b.a(b() + " called onPresentScreen().");
            this.c.onPresentScreen(CustomEventAdapter.this);
        }

        public void onDismissScreen() {
            com.google.ads.util.b.a(b() + " called onDismissScreen().");
            this.c.onDismissScreen(CustomEventAdapter.this);
        }

        public synchronized void onLeaveApplication() {
            com.google.ads.util.b.a(b() + " called onLeaveApplication().");
            this.c.onLeaveApplication(CustomEventAdapter.this);
        }

        public synchronized View a() {
            return this.b;
        }

        private String b() {
            return "Banner custom event labeled '" + CustomEventAdapter.this.a + "'";
        }
    }

    public void requestBannerAd(MediationBannerListener mediationListener, Activity activity, CustomEventServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, CustomEventExtras mediationExtras) {
        com.google.ads.util.a.a((Object) this.a);
        this.a = serverParameters.label;
        String str = serverParameters.className;
        String str2 = serverParameters.parameter;
        this.b = (CustomEventBanner) a(str, CustomEventBanner.class, this.a);
        if (this.b == null) {
            mediationListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
            return;
        }
        com.google.ads.util.a.a((Object) this.c);
        this.c = new a(mediationListener);
        try {
            this.b.requestBannerAd(this.c, activity, this.a, str2, adSize, mediationAdRequest, mediationExtras == null ? null : mediationExtras.getExtra(this.a));
        } catch (Throwable th) {
            a("", th);
            mediationListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
        }
    }

    public View getBannerView() {
        com.google.ads.util.a.b((Object) this.c);
        return this.c.a();
    }

    private class b implements CustomEventInterstitialListener {
        private final MediationInterstitialListener b;

        public b(MediationInterstitialListener mediationInterstitialListener) {
            this.b = mediationInterstitialListener;
        }

        public void onReceivedAd() {
            com.google.ads.util.b.a(a() + " called onReceivedAd.");
            this.b.onReceivedAd(CustomEventAdapter.this);
        }

        public void onFailedToReceiveAd() {
            com.google.ads.util.b.a(a() + " called onFailedToReceiveAd().");
            this.b.onFailedToReceiveAd(CustomEventAdapter.this, AdRequest.ErrorCode.NO_FILL);
        }

        public void onPresentScreen() {
            com.google.ads.util.b.a(a() + " called onPresentScreen().");
            this.b.onPresentScreen(CustomEventAdapter.this);
        }

        public void onDismissScreen() {
            com.google.ads.util.b.a(a() + " called onDismissScreen().");
            this.b.onDismissScreen(CustomEventAdapter.this);
        }

        public synchronized void onLeaveApplication() {
            com.google.ads.util.b.a(a() + " called onLeaveApplication().");
            this.b.onLeaveApplication(CustomEventAdapter.this);
        }

        private String a() {
            return "Interstitial custom event labeled '" + CustomEventAdapter.this.a + "'";
        }
    }

    public void showInterstitial() {
        com.google.ads.util.a.b((Object) this.d);
        try {
            this.d.showInterstitial();
        } catch (Throwable th) {
            com.google.ads.util.b.b("Exception when showing custom event labeled '" + this.a + "'.", th);
        }
    }

    public void requestInterstitialAd(MediationInterstitialListener mediationListener, Activity activity, CustomEventServerParameters serverParameters, MediationAdRequest mediationAdRequest, CustomEventExtras mediationExtras) {
        com.google.ads.util.a.a((Object) this.a);
        this.a = serverParameters.label;
        String str = serverParameters.className;
        String str2 = serverParameters.parameter;
        this.d = (CustomEventInterstitial) a(str, CustomEventInterstitial.class, this.a);
        if (this.d == null) {
            mediationListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
            return;
        }
        try {
            this.d.requestInterstitialAd(new b(mediationListener), activity, this.a, str2, mediationAdRequest, mediationExtras == null ? null : mediationExtras.getExtra(this.a));
        } catch (Throwable th) {
            a("", th);
            mediationListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
        }
    }

    public Class<CustomEventExtras> getAdditionalParametersType() {
        return CustomEventExtras.class;
    }

    public Class<CustomEventServerParameters> getServerParametersType() {
        return CustomEventServerParameters.class;
    }

    public void destroy() {
        if (this.b != null) {
            this.b.destroy();
        }
        if (this.d != null) {
            this.d.destroy();
        }
    }

    private void a(String str, Throwable th) {
        com.google.ads.util.b.b("Error during processing of custom event with label: '" + this.a + "'. Skipping custom event. " + str, th);
    }

    private <T> T a(String str, Class<T> cls, String str2) {
        try {
            return g.a(str, cls);
        } catch (ClassNotFoundException e) {
            a("Make sure you created a visible class named: " + str + ". ", e);
        } catch (ClassCastException e2) {
            a("Make sure your custom event implements the " + cls.getName() + " interface.", e2);
        } catch (IllegalAccessException e3) {
            a("Make sure the default constructor for class " + str + " is visible. ", e3);
        } catch (InstantiationException e4) {
            a("Make sure the name " + str + " does not denote an abstract class or an interface.", e4);
        } catch (Throwable th) {
            a("", th);
        }
        return null;
    }
}
