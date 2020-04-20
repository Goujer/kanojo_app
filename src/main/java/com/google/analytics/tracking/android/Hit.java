package com.google.analytics.tracking.android;

import android.text.TextUtils;

class Hit {
    private final long mHitId;
    private String mHitString;
    private final long mHitTime;
    private String mHitUrlScheme = "https:";

    /* access modifiers changed from: package-private */
    public String getHitParams() {
        return this.mHitString;
    }

    /* access modifiers changed from: package-private */
    public void setHitString(String hitString) {
        this.mHitString = hitString;
    }

    /* access modifiers changed from: package-private */
    public long getHitId() {
        return this.mHitId;
    }

    /* access modifiers changed from: package-private */
    public long getHitTime() {
        return this.mHitTime;
    }

    Hit(String hitString, long hitId, long hitTime) {
        this.mHitString = hitString;
        this.mHitId = hitId;
        this.mHitTime = hitTime;
    }

    /* access modifiers changed from: package-private */
    public String getHitUrlScheme() {
        return this.mHitUrlScheme;
    }

    /* access modifiers changed from: package-private */
    public void setHitUrl(String hitUrl) {
        if (hitUrl != null && !TextUtils.isEmpty(hitUrl.trim()) && hitUrl.toLowerCase().startsWith("http:")) {
            this.mHitUrlScheme = "http:";
        }
    }
}
