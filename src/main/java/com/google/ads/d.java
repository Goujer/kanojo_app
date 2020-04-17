package com.google.ads;

import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

public class d {
    private c a = null;
    private long b = -1;

    public boolean a() {
        return this.a != null && SystemClock.elapsedRealtime() < this.b;
    }

    public void a(c cVar, int i) {
        this.a = cVar;
        this.b = TimeUnit.MILLISECONDS.convert((long) i, TimeUnit.SECONDS) + SystemClock.elapsedRealtime();
    }

    public c b() {
        return this.a;
    }
}
