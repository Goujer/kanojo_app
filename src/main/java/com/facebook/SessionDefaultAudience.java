package com.facebook;

public enum SessionDefaultAudience {
    NONE((String) null),
    ONLY_ME("SELF"),
    FRIENDS("ALL_FRIENDS"),
    EVERYONE("EVERYONE");
    
    private final String nativeProtocolAudience;

    private SessionDefaultAudience(String protocol) {
        this.nativeProtocolAudience = protocol;
    }

    /* access modifiers changed from: package-private */
    public String getNativeProtocolAudience() {
        return this.nativeProtocolAudience;
    }
}
