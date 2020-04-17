package com.google.ads.internal;

import com.google.ads.AdSize;
import com.google.ads.m;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;

public class ActivationOverlay extends AdWebView {
    private volatile boolean b = true;
    private boolean c = true;
    private int d = 0;
    private int e = 0;
    private final i f;

    public ActivationOverlay(n slotState) {
        super(slotState, (AdSize) null);
        if (AdUtil.a < slotState.d.a().b.a().c.a().intValue()) {
            b.a("Disabling hardware acceleration for an activation overlay.");
            g();
        }
        this.f = i.a(slotState.b.a(), a.c, true, true);
        setWebViewClient(this.f);
    }

    public void setOverlayEnabled(boolean overlayEnabled) {
        if (!overlayEnabled) {
            m.a().c.a().post(new Runnable() {
                public void run() {
                    ActivationOverlay.this.a.j.a().removeView(this);
                }
            });
        }
        this.b = overlayEnabled;
    }

    public void setOverlayActivated(boolean overlayActivated) {
        this.c = overlayActivated;
    }

    public boolean a() {
        return this.b;
    }

    public boolean b() {
        return this.c;
    }

    public int c() {
        return this.e;
    }

    public void setYPosition(int yPosition) {
        this.e = yPosition;
    }

    public int d() {
        return this.d;
    }

    public void setXPosition(int xPosition) {
        this.d = xPosition;
    }

    public i e() {
        return this.f;
    }

    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    public boolean canScrollVertically(int direction) {
        return false;
    }
}
