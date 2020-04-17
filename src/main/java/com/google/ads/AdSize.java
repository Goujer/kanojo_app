package com.google.ads;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import jp.co.cybird.app.android.lib.commons.file.json.JSONException;

public class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final AdSize BANNER = new AdSize(320, 50, "mb");
    public static final int FULL_WIDTH = -1;
    public static final AdSize IAB_BANNER = new AdSize(468, 60, "as");
    public static final AdSize IAB_LEADERBOARD = new AdSize(728, 90, "as");
    public static final AdSize IAB_MRECT = new AdSize(300, JSONException.POSTPARSE_ERROR, "as");
    public static final AdSize IAB_WIDE_SKYSCRAPER = new AdSize(160, 600, "as");
    public static final int LANDSCAPE_AD_HEIGHT = 32;
    public static final int LARGE_AD_HEIGHT = 90;
    public static final int PORTRAIT_AD_HEIGHT = 50;
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "mb");
    private final int a;
    private final int b;
    private boolean c;
    private boolean d;
    private boolean e;
    private String f;

    public AdSize(int width, int height) {
        this(width, height, (String) null);
        if (a()) {
            this.e = false;
            this.f = "mb";
            return;
        }
        this.e = true;
    }

    private AdSize(int width, int height, String type) {
        boolean z;
        boolean z2 = true;
        this.a = width;
        this.b = height;
        this.f = type;
        if (width == -1) {
            z = true;
        } else {
            z = false;
        }
        this.c = z;
        this.d = height != -2 ? false : z2;
        this.e = false;
    }

    public static AdSize createAdSize(AdSize adSize, Context context) {
        if (context == null || !adSize.a()) {
            return adSize.a() ? BANNER : adSize;
        }
        AdSize adSize2 = new AdSize(adSize.c ? a(context) : adSize.getWidth(), adSize.d ? b(context) : adSize.getHeight(), adSize.f);
        adSize2.d = adSize.d;
        adSize2.c = adSize.c;
        adSize2.e = adSize.e;
        return adSize2;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AdSize)) {
            return false;
        }
        AdSize adSize = (AdSize) other;
        if (this.a == adSize.a && this.b == adSize.b) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (Integer.valueOf(this.a).hashCode() << 16) | (Integer.valueOf(this.b).hashCode() & 65535);
    }

    public int getWidth() {
        if (this.a >= 0) {
            return this.a;
        }
        throw new UnsupportedOperationException("Ad size was not set before getWidth() was called.");
    }

    public int getHeight() {
        if (this.b >= 0) {
            return this.b;
        }
        throw new UnsupportedOperationException("Ad size was not set before getHeight() was called.");
    }

    private boolean a() {
        return this.a < 0 || this.b < 0;
    }

    public boolean isFullWidth() {
        return this.c;
    }

    public boolean isAutoHeight() {
        return this.d;
    }

    public boolean isCustomAdSize() {
        return this.e;
    }

    public String toString() {
        return getWidth() + "x" + getHeight() + (this.f == null ? "" : "_" + this.f);
    }

    public int getWidthInPixels(Context context) {
        return (int) TypedValue.applyDimension(1, (float) this.a, context.getResources().getDisplayMetrics());
    }

    public int getHeightInPixels(Context context) {
        return (int) TypedValue.applyDimension(1, (float) this.b, context.getResources().getDisplayMetrics());
    }

    public boolean isSizeAppropriate(int width, int height) {
        return ((double) width) <= ((double) this.a) * 1.25d && ((double) width) >= ((double) this.a) * 0.8d && ((double) height) <= ((double) this.b) * 1.25d && ((double) height) >= ((double) this.b) * 0.8d;
    }

    public AdSize findBestSize(AdSize... options) {
        double d2;
        AdSize adSize;
        AdSize adSize2 = null;
        double d3 = 0.0d;
        if (options != null) {
            int length = options.length;
            int i = 0;
            while (i < length) {
                AdSize adSize3 = options[i];
                if (isSizeAppropriate(adSize3.a, adSize3.b)) {
                    d2 = (((double) adSize3.a) * ((double) adSize3.b)) / (((double) this.a) * ((double) this.b));
                    if (d2 > 1.0d) {
                        d2 = 1.0d / d2;
                    }
                    if (d2 > d3) {
                        adSize = adSize3;
                        i++;
                        adSize2 = adSize;
                        d3 = d2;
                    }
                }
                d2 = d3;
                adSize = adSize2;
                i++;
                adSize2 = adSize;
                d3 = d2;
            }
        }
        return adSize2;
    }

    private static int a(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
    }

    private static int b(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        if (i <= 400) {
            return 32;
        }
        if (i <= 720) {
            return 50;
        }
        return 90;
    }
}
