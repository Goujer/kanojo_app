package com.google.ads;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import com.google.ads.g;
import com.google.ads.internal.d;
import com.google.ads.util.a;
import com.google.ads.util.b;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class e {
    /* access modifiers changed from: private */
    public final d a;
    /* access modifiers changed from: private */
    public h b;
    private final Object c;
    /* access modifiers changed from: private */
    public Thread d;
    /* access modifiers changed from: private */
    public final Object e;
    private boolean f;
    private final Object g;

    public e(d dVar) {
        this.b = null;
        this.c = new Object();
        this.d = null;
        this.e = new Object();
        this.f = false;
        this.g = new Object();
        a.b((Object) dVar);
        this.a = dVar;
    }

    public boolean a() {
        boolean z;
        synchronized (this.e) {
            z = this.d != null;
        }
        return z;
    }

    public void b() {
        synchronized (this.g) {
            this.f = true;
            d((h) null);
            synchronized (this.e) {
                if (this.d != null) {
                    this.d.interrupt();
                }
            }
        }
    }

    public void a(final c cVar, final AdRequest adRequest) {
        synchronized (this.e) {
            if (a()) {
                b.c("Mediation thread is not done executing previous mediation  request. Ignoring new mediation request");
                return;
            }
            if (cVar.d()) {
                this.a.a((float) cVar.e());
                if (!this.a.t()) {
                    this.a.g();
                }
            } else if (this.a.t()) {
                this.a.f();
            }
            a(cVar, this.a);
            this.d = new Thread(new Runnable() {
                public void run() {
                    e.this.b(cVar, adRequest);
                    synchronized (e.this.e) {
                        Thread unused = e.this.d = null;
                    }
                }
            });
            this.d.start();
        }
    }

    public static boolean a(c cVar, d dVar) {
        if (cVar.j() == null) {
            return true;
        }
        if (!dVar.i().b()) {
            AdSize c2 = dVar.i().g.a().c();
            if (cVar.j().a()) {
                b.e("AdView received a mediation response corresponding to an interstitial ad. Make sure you specify the banner ad size corresponding to the AdSize you used in your AdView  (" + c2 + ") in the ad-type field in the mediation UI.");
                return false;
            }
            AdSize c3 = cVar.j().c();
            if (c3 == c2) {
                return true;
            }
            b.e("Mediation server returned ad size: '" + c3 + "', while the AdView was created with ad size: '" + c2 + "'. Using the ad-size passed to the AdView on creation.");
            return false;
        } else if (cVar.j().a()) {
            return true;
        } else {
            b.e("InterstitialAd received a mediation response corresponding to a non-interstitial ad. Make sure you specify 'interstitial' as the ad-type in the mediation UI.");
            return false;
        }
    }

    private boolean a(h hVar, String str) {
        if (e() == hVar) {
            return true;
        }
        b.c("GWController: ignoring callback to " + str + " from non showing ambassador with adapter class: '" + hVar.h() + "'.");
        return false;
    }

    public void a(h hVar, final boolean z) {
        if (a(hVar, "onAdClicked()")) {
            final f a2 = hVar.a();
            m.a().c.a().post(new Runnable() {
                public void run() {
                    e.this.a.a(a2, z);
                }
            });
        }
    }

    public void a(h hVar, final View view) {
        if (e() != hVar) {
            b.c("GWController: ignoring onAdRefreshed() callback from non-showing ambassador (adapter class name is '" + hVar.h() + "').");
            return;
        }
        this.a.n().a(g.a.AD);
        final f a2 = this.b.a();
        m.a().c.a().post(new Runnable() {
            public void run() {
                e.this.a.a(view, e.this.b, a2, true);
            }
        });
    }

    public void a(h hVar) {
        if (a(hVar, "onPresentScreen")) {
            m.a().c.a().post(new Runnable() {
                public void run() {
                    e.this.a.v();
                }
            });
        }
    }

    public void b(h hVar) {
        if (a(hVar, "onDismissScreen")) {
            m.a().c.a().post(new Runnable() {
                public void run() {
                    e.this.a.u();
                }
            });
        }
    }

    public void c(h hVar) {
        if (a(hVar, "onLeaveApplication")) {
            m.a().c.a().post(new Runnable() {
                public void run() {
                    e.this.a.w();
                }
            });
        }
    }

    public boolean c() {
        a.a(this.a.i().b());
        h e2 = e();
        if (e2 != null) {
            e2.g();
            return true;
        }
        b.b("There is no ad ready to show.");
        return false;
    }

    protected e() {
        this.b = null;
        this.c = new Object();
        this.d = null;
        this.e = new Object();
        this.f = false;
        this.g = new Object();
        this.a = null;
    }

    private boolean d() {
        boolean z;
        synchronized (this.g) {
            z = this.f;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void b(final c cVar, AdRequest adRequest) {
        synchronized (this.e) {
            a.a((Object) Thread.currentThread(), (Object) this.d);
        }
        List<a> f2 = cVar.f();
        long b2 = cVar.a() ? (long) cVar.b() : 10000;
        for (a next : f2) {
            b.a("Looking to fetch ads from network: " + next.b());
            List<String> c2 = next.c();
            HashMap<String, String> e2 = next.e();
            List<String> d2 = next.d();
            String a2 = next.a();
            String b3 = next.b();
            String c3 = cVar.c();
            if (d2 == null) {
                d2 = cVar.g();
            }
            f fVar = new f(a2, b3, c3, d2, cVar.h(), cVar.i());
            Iterator<String> it = c2.iterator();
            while (true) {
                if (it.hasNext()) {
                    String next2 = it.next();
                    Activity a3 = this.a.i().c.a();
                    if (a3 == null) {
                        b.a("Activity is null while mediating.  Terminating mediation thread.");
                        return;
                    }
                    this.a.n().c();
                    if (a(next2, a3, adRequest, fVar, e2, b2)) {
                        return;
                    }
                    if (d()) {
                        b.a("GWController.destroy() called. Terminating mediation thread.");
                        return;
                    }
                }
            }
        }
        m.a().c.a().post(new Runnable() {
            public void run() {
                e.this.a.b(cVar);
            }
        });
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    private boolean a(String str, Activity activity, AdRequest adRequest, final f fVar, HashMap<String, String> hashMap, long j) {
        final h hVar = new h(this, this.a.i().g.a(), fVar, str, adRequest, hashMap);
        synchronized (hVar) {
            hVar.a(activity);
            while (!hVar.c() && j > 0) {
                try {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    hVar.wait(j);
                    j -= SystemClock.elapsedRealtime() - elapsedRealtime;
                } catch (InterruptedException e2) {
                    b.a("Interrupted while waiting for ad network to load ad using adapter class: " + str);
                }
            }
            this.a.n().a(hVar.e());
            if (!hVar.c() || !hVar.d()) {
                if (!hVar.c()) {
                    b.a("Timeout occurred in adapter class: " + hVar.h());
                }
                hVar.b();
                return false;
            }
            final View f2 = this.a.i().b() ? null : hVar.f();
            m.a().c.a().post(new Runnable() {
                public void run() {
                    if (e.this.e(hVar)) {
                        b.a("Trying to switch GWAdNetworkAmbassadors, but GWController().destroy() has been called. Destroying the new ambassador and terminating mediation.");
                    } else {
                        e.this.a.a(f2, hVar, fVar, false);
                    }
                }
            });
            return true;
        }
    }

    /* access modifiers changed from: private */
    public boolean e(h hVar) {
        boolean z;
        synchronized (this.g) {
            if (d()) {
                hVar.b();
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    private h e() {
        h hVar;
        synchronized (this.c) {
            hVar = this.b;
        }
        return hVar;
    }

    public void d(h hVar) {
        synchronized (this.c) {
            if (this.b != hVar) {
                if (this.b != null) {
                    this.b.b();
                }
                this.b = hVar;
            }
        }
    }
}
