package com.google.ads.util;

import android.os.Build;

class d {
    static final d d = new d();
    static final d e = new d("unknown", "generic", "generic");
    static final d f = new d("unknown", "generic_x86", "Android");
    public final String a;
    public final String b;
    public final String c;

    d() {
        this.a = Build.BOARD;
        this.b = Build.DEVICE;
        this.c = Build.BRAND;
    }

    d(String str, String str2, String str3) {
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    public boolean equals(Object o) {
        if (!(o instanceof d)) {
            return false;
        }
        d dVar = (d) o;
        if (!a(this.a, dVar.a) || !a(this.b, dVar.b) || !a(this.c, dVar.c)) {
            return false;
        }
        return true;
    }

    private static boolean a(String str, String str2) {
        if (str != null) {
            return str.equals(str2);
        }
        return str == str2;
    }

    public int hashCode() {
        int i = 0;
        if (this.a != null) {
            i = 0 + this.a.hashCode();
        }
        if (this.b != null) {
            i += this.b.hashCode();
        }
        if (this.c != null) {
            return i + this.c.hashCode();
        }
        return i;
    }
}
