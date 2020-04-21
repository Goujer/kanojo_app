package com.google.ads.mediation.admob;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;
import java.util.Map;

public class AdMobAdapterExtras implements NetworkExtras {
    private boolean a;
    private Map<String, Object> b;

    public AdMobAdapterExtras() {
        this.a = false;
        clearExtras();
    }

    public AdMobAdapterExtras(AdMobAdapterExtras original) {
        this();
        if (original != null) {
            this.a = original.a;
            this.b.putAll(original.b);
        }
    }

    @Deprecated
    public AdMobAdapterExtras setPlusOneOptOut(boolean plusOneOptOut) {
        return this;
    }

    @Deprecated
    public boolean getPlusOneOptOut() {
        return false;
    }

    public AdMobAdapterExtras setUseExactAdSize(boolean useExactAdSize) {
        this.a = useExactAdSize;
        return this;
    }

    public boolean getUseExactAdSize() {
        return this.a;
    }

    public Map<String, Object> getExtras() {
        return this.b;
    }

    public AdMobAdapterExtras setExtras(Map<String, Object> extras) {
        if (extras == null) {
            throw new IllegalArgumentException("Argument 'extras' may not be null");
        }
        this.b = extras;
        return this;
    }

    public AdMobAdapterExtras clearExtras() {
        this.b = new HashMap();
        return this;
    }

    public AdMobAdapterExtras addExtra(String key, Object value) {
        this.b.put(key, value);
        return this;
    }
}
