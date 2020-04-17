package com.google.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.ads.Ad;
import com.google.ads.AdActivity;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AppEventListener;
import com.google.ads.InterstitialAd;
import com.google.ads.ae;
import com.google.ads.af;
import com.google.ads.at;
import com.google.ads.c;
import com.google.ads.doubleclick.SwipeableDfpAdView;
import com.google.ads.e;
import com.google.ads.f;
import com.google.ads.g;
import com.google.ads.h;
import com.google.ads.l;
import com.google.ads.m;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.IcsUtil;
import com.google.ads.util.a;
import com.google.ads.util.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class d {
    private static final Object a = new Object();
    private String A = null;
    private String B = null;
    private final n b;
    private c c;
    private AdRequest d;
    private g e;
    private AdWebView f;
    private i g;
    private boolean h = false;
    private long i;
    private boolean j;
    private boolean k;
    private boolean l;
    private boolean m;
    private boolean n;
    private SharedPreferences o;
    private long p;
    private af q;
    private boolean r;
    private LinkedList<String> s;
    private LinkedList<String> t;
    private LinkedList<String> u;
    private int v = -1;
    private Boolean w;
    private com.google.ads.d x;
    private e y;
    private f z;

    public d(Ad ad, Activity activity, AdSize adSize, String str, ViewGroup viewGroup, boolean z2) {
        AdView adView;
        InterstitialAd interstitialAd;
        AdView adView2;
        InterstitialAd interstitialAd2;
        this.r = z2;
        this.e = new g();
        this.c = null;
        this.d = null;
        this.k = false;
        this.p = 60000;
        this.l = false;
        this.n = false;
        this.m = true;
        h a2 = adSize == null ? h.a : h.a(adSize, activity.getApplicationContext());
        if (ad instanceof SwipeableDfpAdView) {
            a2.a(true);
        }
        if (activity == null) {
            m a3 = m.a();
            if (ad instanceof AdView) {
                adView2 = (AdView) ad;
            } else {
                adView2 = null;
            }
            if (ad instanceof InterstitialAd) {
                interstitialAd2 = (InterstitialAd) ad;
            } else {
                interstitialAd2 = null;
            }
            this.b = new n(a3, ad, adView2, interstitialAd2, str, (Activity) null, (Context) null, viewGroup, a2, this);
            return;
        }
        synchronized (a) {
            this.o = activity.getApplicationContext().getSharedPreferences("GoogleAdMobAdsPrefs", 0);
            if (z2) {
                long j2 = this.o.getLong("Timeout" + str, -1);
                if (j2 < 0) {
                    this.i = 5000;
                } else {
                    this.i = j2;
                }
            } else {
                this.i = 60000;
            }
        }
        m a4 = m.a();
        if (ad instanceof AdView) {
            adView = (AdView) ad;
        } else {
            adView = null;
        }
        if (ad instanceof InterstitialAd) {
            interstitialAd = (InterstitialAd) ad;
        } else {
            interstitialAd = null;
        }
        this.b = new n(a4, ad, adView, interstitialAd, str, activity, activity.getApplicationContext(), viewGroup, a2, this);
        this.q = new af(this);
        this.s = new LinkedList<>();
        this.t = new LinkedList<>();
        this.u = new LinkedList<>();
        a();
        AdUtil.h(this.b.f.a());
        this.x = new com.google.ads.d();
        this.y = new e(this);
        this.w = null;
        this.z = null;
    }

    public synchronized void a() {
        AdSize c2 = this.b.g.a().c();
        this.f = AdUtil.a >= 14 ? new IcsUtil.IcsAdWebView(this.b, c2) : new AdWebView(this.b, c2);
        this.f.setVisibility(8);
        this.g = i.a(this, a.d, true, this.b.b());
        this.f.setWebViewClient(this.g);
        if (AdUtil.a < this.b.d.a().b.a().b.a().intValue() && !this.b.g.a().a()) {
            b.a("Disabling hardware acceleration for a banner.");
            this.f.g();
        }
    }

    public synchronized void b() {
        if (this.y != null) {
            this.y.b();
        }
        this.b.o.a(null);
        this.b.p.a(null);
        C();
        f();
        if (this.f != null) {
            this.f.destroy();
        }
        this.h = true;
    }

    public void a(String str) {
        this.B = str;
        Uri build = new Uri.Builder().encodedQuery(str).build();
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> b2 = AdUtil.b(build);
        for (String next : b2.keySet()) {
            sb.append(next).append(" = ").append(b2.get(next)).append("\n");
        }
        this.A = sb.toString().trim();
        if (TextUtils.isEmpty(this.A)) {
            this.A = null;
        }
    }

    public String c() {
        return this.A;
    }

    public String d() {
        return this.B;
    }

    public synchronized void e() {
        this.m = false;
        b.a("Refreshing is no longer allowed on this AdView.");
    }

    public synchronized void f() {
        if (this.l) {
            b.a("Disabling refreshing.");
            m.a().c.a().removeCallbacks(this.q);
            this.l = false;
        } else {
            b.a("Refreshing is already disabled.");
        }
    }

    public synchronized void g() {
        this.n = false;
        if (!this.b.a()) {
            b.a("Tried to enable refreshing on something other than an AdView.");
        } else if (!this.m) {
            b.a("Refreshing disabled on this AdView");
        } else if (!this.l) {
            b.a("Enabling refreshing every " + this.p + " milliseconds.");
            m.a().c.a().postDelayed(this.q, this.p);
            this.l = true;
        } else {
            b.a("Refreshing is already enabled.");
        }
    }

    public void h() {
        g();
        this.n = true;
    }

    public n i() {
        return this.b;
    }

    public synchronized com.google.ads.d j() {
        return this.x;
    }

    public synchronized c k() {
        return this.c;
    }

    public synchronized AdWebView l() {
        return this.f;
    }

    public synchronized i m() {
        return this.g;
    }

    public g n() {
        return this.e;
    }

    public synchronized void a(int i2) {
        this.v = i2;
    }

    public synchronized int o() {
        return this.v;
    }

    public long p() {
        return this.i;
    }

    public synchronized boolean q() {
        return this.c != null;
    }

    public synchronized boolean r() {
        return this.j;
    }

    public synchronized boolean s() {
        return this.k;
    }

    public synchronized boolean t() {
        return this.l;
    }

    public synchronized void a(AdRequest adRequest) {
        b.d("v6.4.1 RC00");
        if (this.h) {
            b.e("loadAd called after ad was destroyed.");
        } else if (q()) {
            b.e("loadAd called while the ad is already loading, so aborting.");
        } else if (AdActivity.isShowing()) {
            b.e("loadAd called while an interstitial or landing page is displayed, so aborting");
        } else if (AdUtil.c(this.b.f.a()) && AdUtil.b(this.b.f.a())) {
            if (at.a(this.b.f.a(), this.o.getLong("GoogleAdMobDoritosLife", 60000))) {
                at.a(this.b.c.a());
            }
            this.k = false;
            this.s.clear();
            this.t.clear();
            this.d = adRequest;
            if (this.x.a()) {
                this.y.a(this.x.b(), adRequest);
            } else {
                l lVar = new l(this.b);
                this.b.m.a(lVar);
                this.c = lVar.b.a();
                this.c.a(adRequest);
            }
        }
    }

    public synchronized void a(AdRequest.ErrorCode errorCode) {
        this.c = null;
        if (errorCode == AdRequest.ErrorCode.NETWORK_ERROR) {
            a(60.0f);
            if (!t()) {
                h();
            }
        }
        if (this.b.b()) {
            if (errorCode == AdRequest.ErrorCode.NO_FILL) {
                this.e.B();
            } else if (errorCode == AdRequest.ErrorCode.NETWORK_ERROR) {
                this.e.z();
            }
        }
        b.c("onFailedToReceiveAd(" + errorCode + ")");
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onFailedToReceiveAd(this.b.a.a(), errorCode);
        }
    }

    public synchronized void a(c cVar) {
        this.c = null;
        this.y.a(cVar, this.d);
    }

    public synchronized void a(View view, h hVar, f fVar, boolean z2) {
        b.a("AdManager.onReceiveGWhirlAd() called.");
        this.k = true;
        this.z = fVar;
        if (this.b.a()) {
            a(view);
            a(fVar, Boolean.valueOf(z2));
        }
        this.y.d(hVar);
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onReceiveAd(this.b.a.a());
        }
    }

    public synchronized void a(f fVar, boolean z2) {
        b.a(String.format(Locale.US, "AdManager.onGWhirlAdClicked(%b) called.", new Object[]{Boolean.valueOf(z2)}));
        b(fVar, Boolean.valueOf(z2));
    }

    public synchronized void b(c cVar) {
        b.a("AdManager.onGWhirlNoFill() called.");
        a(cVar.i(), cVar.c());
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onFailedToReceiveAd(this.b.a.a(), AdRequest.ErrorCode.NO_FILL);
        }
    }

    public synchronized void u() {
        this.e.C();
        b.c("onDismissScreen()");
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onDismissScreen(this.b.a.a());
        }
    }

    public synchronized void v() {
        b.c("onPresentScreen()");
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onPresentScreen(this.b.a.a());
        }
    }

    public synchronized void w() {
        b.c("onLeaveApplication()");
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onLeaveApplication(this.b.a.a());
        }
    }

    public synchronized void a(String str, String str2) {
        AppEventListener a2 = this.b.p.a();
        if (a2 != null) {
            a2.onAppEvent(this.b.a.a(), str, str2);
        }
    }

    public void x() {
        this.e.f();
        D();
    }

    private void a(f fVar, Boolean bool) {
        List d2 = fVar.d();
        if (d2 == null) {
            d2 = new ArrayList();
            d2.add("http://e.admob.com/imp?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@&adt=@gw_adt@&aec=@gw_aec@");
        }
        String b2 = fVar.b();
        a(d2, fVar.a(), b2, fVar.c(), bool, this.e.d(), this.e.e());
    }

    private void b(f fVar, Boolean bool) {
        List e2 = fVar.e();
        if (e2 == null) {
            e2 = new ArrayList();
            e2.add("http://e.admob.com/clk?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@");
        }
        a(e2, fVar.a(), fVar.b(), fVar.c(), bool, (String) null, (String) null);
    }

    private void a(List<String> list, String str) {
        List<String> list2;
        if (list == null) {
            list2 = new ArrayList<>();
            list2.add("http://e.admob.com/nofill?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&adt=@gw_adt@&aec=@gw_aec@");
        } else {
            list2 = list;
        }
        a(list2, (String) null, (String) null, str, (Boolean) null, this.e.d(), this.e.e());
    }

    private void a(List<String> list, String str, String str2, String str3, Boolean bool, String str4, String str5) {
        String a2 = AdUtil.a(this.b.f.a());
        com.google.ads.b a3 = com.google.ads.b.a();
        String bigInteger = a3.b().toString();
        String bigInteger2 = a3.c().toString();
        for (String a4 : list) {
            new Thread(new ae(g.a(a4, this.b.h.a(), bool, a2, str, str2, str3, bigInteger, bigInteger2, str4, str5), this.b.f.a())).start();
        }
        this.e.b();
    }

    public synchronized void y() {
        Activity a2 = this.b.c.a();
        if (a2 == null) {
            b.e("activity was null while trying to ping tracking URLs.");
        } else {
            Iterator it = this.s.iterator();
            while (it.hasNext()) {
                new Thread(new ae((String) it.next(), a2.getApplicationContext())).start();
            }
        }
    }

    public synchronized void z() {
        Activity a2 = this.b.c.a();
        if (a2 == null) {
            b.e("activity was null while trying to ping manual tracking URLs.");
        } else {
            Iterator it = this.t.iterator();
            while (it.hasNext()) {
                new Thread(new ae((String) it.next(), a2.getApplicationContext())).start();
            }
        }
    }

    public synchronized void A() {
        if (!this.h) {
            if (this.d == null) {
                b.a("Tried to refresh before calling loadAd().");
            } else if (this.b.a()) {
                if (!this.b.j.a().isShown() || !AdUtil.d()) {
                    b.a("Not refreshing because the ad is not visible.");
                } else {
                    b.c("Refreshing ad.");
                    a(this.d);
                }
                if (this.n) {
                    f();
                } else {
                    m.a().c.a().postDelayed(this.q, this.p);
                }
            } else {
                b.a("Tried to refresh an ad that wasn't an AdView.");
            }
        }
    }

    public void a(long j2) {
        synchronized (a) {
            SharedPreferences.Editor edit = this.o.edit();
            edit.putLong("Timeout" + this.b.h, j2);
            edit.commit();
            if (this.r) {
                this.i = j2;
            }
        }
    }

    public synchronized void a(boolean z2) {
        this.j = z2;
    }

    public void a(View view) {
        this.b.i.a().setVisibility(0);
        this.b.i.a().removeAllViews();
        this.b.i.a().addView(view);
        if (this.b.g.a().b()) {
            this.b.b.a().a(this.b.l.a(), false, -1, -1, -1, -1);
            if (this.b.e.a().a()) {
                this.b.i.a().addView(this.b.e.a(), AdUtil.a(this.b.f.a(), this.b.g.a().c().getWidth()), AdUtil.a(this.b.f.a(), this.b.g.a().c().getHeight()));
            }
        }
    }

    public synchronized void a(float f2) {
        long j2 = this.p;
        this.p = (long) (1000.0f * f2);
        if (t() && this.p != j2) {
            f();
            g();
        }
    }

    public synchronized void b(long j2) {
        if (j2 > 0) {
            this.o.edit().putLong("GoogleAdMobDoritosLife", j2).commit();
        }
    }

    public synchronized void B() {
        a.a(this.b.b());
        if (this.k) {
            this.k = false;
            if (this.w == null) {
                b.b("isMediationFlag is null in show() with isReady() true. we should have an ad and know whether this is a mediation request or not. ");
            } else if (!this.w.booleanValue()) {
                AdActivity.launchAdActivity(this, new e("interstitial"));
                y();
            } else if (this.y.c()) {
                a(this.z, (Boolean) false);
            }
        } else {
            b.c("Cannot show interstitial because it is not loaded and ready.");
        }
    }

    public synchronized void C() {
        if (this.c != null) {
            this.c.a();
            this.c = null;
        }
        if (this.f != null) {
            this.f.stopLoading();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void D() {
        Activity a2 = this.b.c.a();
        if (a2 == null) {
            b.e("activity was null while trying to ping click tracking URLs.");
        } else {
            Iterator it = this.u.iterator();
            while (it.hasNext()) {
                new Thread(new ae((String) it.next(), a2.getApplicationContext())).start();
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void E() {
        this.c = null;
        this.k = true;
        this.f.setVisibility(0);
        if (this.b.a()) {
            a((View) this.f);
        }
        this.e.g();
        if (this.b.a()) {
            y();
        }
        b.c("onReceiveAd()");
        AdListener a2 = this.b.o.a();
        if (a2 != null) {
            a2.onReceiveAd(this.b.a.a());
        }
        this.b.l.a(this.b.m.a());
        this.b.m.a(null);
    }

    /* access modifiers changed from: protected */
    public synchronized void b(String str) {
        b.a("Adding a tracking URL: " + str);
        this.s.add(str);
    }

    /* access modifiers changed from: protected */
    public synchronized void c(String str) {
        b.a("Adding a manual tracking URL: " + str);
        F().add(str);
    }

    /* access modifiers changed from: protected */
    public synchronized void a(LinkedList<String> linkedList) {
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            b.a("Adding a click tracking URL: " + ((String) it.next()));
        }
        this.u = linkedList;
    }

    public void b(boolean z2) {
        this.w = Boolean.valueOf(z2);
    }

    public void a(l lVar, boolean z2, int i2, int i3, int i4, int i5) {
        this.b.e.a().setOverlayActivated(!z2);
        a(i2, i3, i4, i5);
        if (this.b.q.a() == null) {
            return;
        }
        if (z2) {
            this.b.q.a().onAdActivated(this.b.a.a());
        } else {
            this.b.q.a().onAdDeactivated(this.b.a.a());
        }
    }

    public void a(int i2, int i3, int i4, int i5) {
        int i6;
        int i7;
        int i8;
        ActivationOverlay a2 = this.b.e.a();
        int a3 = AdUtil.a(this.b.f.a(), i4 < 0 ? this.b.g.a().c().getWidth() : i4);
        Context a4 = this.b.f.a();
        if (i5 < 0) {
            i5 = this.b.g.a().c().getHeight();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(a3, AdUtil.a(a4, i5));
        if (i4 < 0) {
            i6 = 0;
            i7 = 0;
        } else {
            i6 = i3;
            i7 = i2;
        }
        if (i7 < 0) {
            i8 = this.b.e.a().d();
        } else {
            i8 = i7;
        }
        if (i6 < 0) {
            i6 = this.b.e.a().c();
        }
        this.b.e.a().setXPosition(i8);
        this.b.e.a().setYPosition(i6);
        layoutParams.setMargins(AdUtil.a(this.b.f.a(), i8), AdUtil.a(this.b.f.a(), i6), 0, 0);
        a2.setLayoutParams(layoutParams);
    }

    public LinkedList<String> F() {
        return this.t;
    }
}
