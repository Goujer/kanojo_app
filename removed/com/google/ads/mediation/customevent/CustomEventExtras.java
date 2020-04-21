package com.google.ads.mediation.customevent;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;

public class CustomEventExtras implements NetworkExtras {
    private final HashMap<String, Object> a = new HashMap<>();

    public CustomEventExtras addExtra(String label, Object value) {
        this.a.put(label, value);
        return this;
    }

    public CustomEventExtras clearExtras() {
        this.a.clear();
        return this;
    }

    public Object getExtra(String label) {
        return this.a.get(label);
    }

    public Object removeExtra(String label) {
        return this.a.remove(label);
    }
}
