package com.google.ads.internal;

import android.os.SystemClock;
import com.google.ads.g;
import com.google.ads.util.b;
import java.util.Iterator;
import java.util.LinkedList;

public class g {
    private static long f = 0;
    private static long g = 0;
    private static long h = 0;
    private static long i = 0;
    private static long j = -1;
    private final LinkedList<Long> a = new LinkedList<>();
    private long b;
    private long c;
    private long d;
    private final LinkedList<Long> e = new LinkedList<>();
    private boolean k = false;
    private boolean l = false;
    private String m;
    private long n;
    private final LinkedList<Long> o = new LinkedList<>();
    private final LinkedList<g.a> p = new LinkedList<>();

    public g() {
        a();
    }

    /* access modifiers changed from: protected */
    public synchronized void a() {
        this.a.clear();
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this.e.clear();
        this.n = -1;
        this.o.clear();
        this.p.clear();
        this.k = false;
        this.l = false;
    }

    public synchronized void b() {
        this.o.clear();
        this.p.clear();
    }

    public synchronized void c() {
        this.n = SystemClock.elapsedRealtime();
    }

    public synchronized void a(g.a aVar) {
        this.o.add(Long.valueOf(SystemClock.elapsedRealtime() - this.n));
        this.p.add(aVar);
    }

    public synchronized String d() {
        StringBuilder sb;
        sb = new StringBuilder();
        Iterator it = this.o.iterator();
        while (it.hasNext()) {
            long longValue = ((Long) it.next()).longValue();
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(longValue);
        }
        return sb.toString();
    }

    public synchronized String e() {
        StringBuilder sb;
        sb = new StringBuilder();
        Iterator it = this.p.iterator();
        while (it.hasNext()) {
            g.a aVar = (g.a) it.next();
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(aVar.ordinal());
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void f() {
        b.d("Ad clicked.");
        this.a.add(Long.valueOf(SystemClock.elapsedRealtime()));
    }

    /* access modifiers changed from: protected */
    public void g() {
        b.d("Ad request loaded.");
        this.b = SystemClock.elapsedRealtime();
    }

    /* access modifiers changed from: protected */
    public synchronized void h() {
        b.d("Ad request before rendering.");
        this.c = SystemClock.elapsedRealtime();
    }

    /* access modifiers changed from: protected */
    public void i() {
        b.d("Ad request started.");
        this.d = SystemClock.elapsedRealtime();
        f++;
    }

    /* access modifiers changed from: protected */
    public long j() {
        if (this.a.size() != this.e.size()) {
            return -1;
        }
        return (long) this.a.size();
    }

    /* access modifiers changed from: protected */
    public String k() {
        if (this.a.isEmpty() || this.a.size() != this.e.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.a.size()) {
                return sb.toString();
            }
            if (i3 != 0) {
                sb.append(",");
            }
            sb.append(Long.toString(this.e.get(i3).longValue() - this.a.get(i3).longValue()));
            i2 = i3 + 1;
        }
    }

    /* access modifiers changed from: protected */
    public String l() {
        if (this.a.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.a.size()) {
                return sb.toString();
            }
            if (i3 != 0) {
                sb.append(",");
            }
            sb.append(Long.toString(this.a.get(i3).longValue() - this.b));
            i2 = i3 + 1;
        }
    }

    /* access modifiers changed from: protected */
    public long m() {
        return this.b - this.d;
    }

    /* access modifiers changed from: protected */
    public synchronized long n() {
        return this.c - this.d;
    }

    /* access modifiers changed from: protected */
    public long o() {
        return f;
    }

    /* access modifiers changed from: protected */
    public synchronized long p() {
        return g;
    }

    /* access modifiers changed from: protected */
    public synchronized void q() {
        b.d("Ad request network error");
        g++;
    }

    /* access modifiers changed from: protected */
    public synchronized void r() {
        g = 0;
    }

    /* access modifiers changed from: protected */
    public synchronized long s() {
        return h;
    }

    /* access modifiers changed from: protected */
    public synchronized void t() {
        h++;
    }

    /* access modifiers changed from: protected */
    public synchronized void u() {
        h = 0;
    }

    /* access modifiers changed from: protected */
    public synchronized long v() {
        return i;
    }

    /* access modifiers changed from: protected */
    public synchronized void w() {
        i++;
    }

    /* access modifiers changed from: protected */
    public synchronized void x() {
        i = 0;
    }

    /* access modifiers changed from: protected */
    public boolean y() {
        return this.k;
    }

    /* access modifiers changed from: protected */
    public void z() {
        b.d("Interstitial network error.");
        this.k = true;
    }

    /* access modifiers changed from: protected */
    public boolean A() {
        return this.l;
    }

    /* access modifiers changed from: protected */
    public void B() {
        b.d("Interstitial no fill.");
        this.l = true;
    }

    public void C() {
        b.d("Landing page dismissed.");
        this.e.add(Long.valueOf(SystemClock.elapsedRealtime()));
    }

    /* access modifiers changed from: protected */
    public String D() {
        return this.m;
    }

    public void a(String str) {
        b.d("Prior impression ticket = " + str);
        this.m = str;
    }

    public static long E() {
        if (j != -1) {
            return SystemClock.elapsedRealtime() - j;
        }
        j = SystemClock.elapsedRealtime();
        return 0;
    }
}
