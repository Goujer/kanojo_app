package com.google.ads.internal;

import android.content.Context;
import com.google.ads.AdSize;

public class h {
    public static final h a = new h((AdSize) null, true);
    private AdSize b;
    private boolean c;
    private final boolean d;

    private h(AdSize adSize, boolean z) {
        this.b = adSize;
        this.d = z;
    }

    public static h a(AdSize adSize, Context context) {
        return new h(AdSize.createAdSize(adSize, context), false);
    }

    public static h a(AdSize adSize) {
        return a(adSize, (Context) null);
    }

    public boolean a() {
        return this.d;
    }

    public boolean b() {
        return this.c;
    }

    public AdSize c() {
        return this.b;
    }

    public void b(AdSize adSize) {
        if (!this.d) {
            this.b = adSize;
        }
    }

    public void a(boolean z) {
        this.c = z;
    }
}
