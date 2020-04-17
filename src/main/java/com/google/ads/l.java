package com.google.ads;

import com.google.ads.internal.c;
import com.google.ads.util.i;

public final class l extends i {
    public final i.b<n> a;
    public final i.c<c> b;
    public final i.c<Boolean> c = new i.c<>("disableNativeScroll", false);

    public l(n nVar) {
        this.a = new i.b<>("slotState", nVar);
        this.b = new i.c<>("adLoader", new c(this));
    }
}
