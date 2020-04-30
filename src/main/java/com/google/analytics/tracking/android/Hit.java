package com.google.analytics.tracking.android;

import android.text.TextUtils;

class Hit {
    private final long mHitId;
    private String mHitString;
    private final long mHitTime;
    private String mHitUrlScheme = "https:";

    String getHitParams() {
        return this.mHitString;
    }

	void setHitString(String hitString) {
        this.mHitString = hitString;
    }

	long getHitId() {
        return this.mHitId;
    }

    long getHitTime() {
        return this.mHitTime;
    }

    Hit(String hitString, long hitId, long hitTime) {
        this.mHitString = hitString;
        this.mHitId = hitId;
        this.mHitTime = hitTime;
    }

    String getHitUrlScheme() {
        return this.mHitUrlScheme;
    }

    void setHitUrl(String hitUrl) {
        if (hitUrl != null && !TextUtils.isEmpty(hitUrl.trim()) && hitUrl.toLowerCase().startsWith("http:")) {
            this.mHitUrlScheme = "http:";
        }
    }
}
