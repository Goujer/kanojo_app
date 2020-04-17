package com.facebook;

public enum SessionLoginBehavior {
    SSO_WITH_FALLBACK(true, true),
    SSO_ONLY(true, false),
    SUPPRESS_SSO(false, true);
    
    private final boolean allowsKatanaAuth;
    private final boolean allowsWebViewAuth;

    private SessionLoginBehavior(boolean allowsKatanaAuth2, boolean allowsWebViewAuth2) {
        this.allowsKatanaAuth = allowsKatanaAuth2;
        this.allowsWebViewAuth = allowsWebViewAuth2;
    }

    /* access modifiers changed from: package-private */
    public boolean allowsKatanaAuth() {
        return this.allowsKatanaAuth;
    }

    /* access modifiers changed from: package-private */
    public boolean allowsWebViewAuth() {
        return this.allowsWebViewAuth;
    }
}
