package com.google.analytics.tracking.android;

import android.text.TextUtils;
import com.google.analytics.tracking.android.GAUsage;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class Tracker {
    static final long MAX_TOKENS = 120000;
    static final long NUM_TOKENS_PER_HIT = 2000;
    private final AppFieldsDefaultProvider mAppFieldsDefaultProvider;
    private final ClientIdDefaultProvider mClientIdDefaultProvider;
    private final TrackerHandler mHandler;
    private long mLastTrackTime;
    private final String mName;
    private final Map<String, String> mParams;
    private final ScreenResolutionDefaultProvider mScreenResolutionDefaultProvider;
    private long mTokens;

    Tracker(String name, String trackingId, TrackerHandler handler) {
        this(name, trackingId, handler, ClientIdDefaultProvider.getProvider(), ScreenResolutionDefaultProvider.getProvider(), AppFieldsDefaultProvider.getProvider());
    }

    @VisibleForTesting
    Tracker(String name, String trackingId, TrackerHandler handler, ClientIdDefaultProvider clientIdDefaultProvider, ScreenResolutionDefaultProvider screenResolutionDefaultProvider, AppFieldsDefaultProvider appFieldsDefaultProvider) {
        this.mParams = new HashMap();
        this.mTokens = MAX_TOKENS;
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Tracker name cannot be empty.");
        }
        this.mName = name;
        this.mHandler = handler;
        this.mParams.put(Fields.TRACKING_ID, trackingId);
        this.mParams.put(Fields.USE_SECURE, GreeDefs.KANOJO_NAME);
        this.mClientIdDefaultProvider = clientIdDefaultProvider;
        this.mScreenResolutionDefaultProvider = screenResolutionDefaultProvider;
        this.mAppFieldsDefaultProvider = appFieldsDefaultProvider;
    }

    public String getName() {
        GAUsage.getInstance().setUsage(GAUsage.Field.GET_TRACKER_NAME);
        return this.mName;
    }

    public void send(Map<String, String> params) {
        GAUsage.getInstance().setUsage(GAUsage.Field.SEND);
        Map<String, String> paramsToSend = new HashMap<>();
        paramsToSend.putAll(this.mParams);
        if (params != null) {
            paramsToSend.putAll(params);
        }
        if (TextUtils.isEmpty(paramsToSend.get(Fields.TRACKING_ID))) {
            Log.w(String.format("Missing tracking id (%s) parameter.", new Object[]{Fields.TRACKING_ID}));
        }
        String hitType = paramsToSend.get(Fields.HIT_TYPE);
        if (TextUtils.isEmpty(hitType)) {
            Log.w(String.format("Missing hit type (%s) parameter.", new Object[]{Fields.HIT_TYPE}));
            hitType = "";
        }
        if (hitType.equals(HitTypes.TRANSACTION) || hitType.equals(HitTypes.ITEM) || tokensAvailable()) {
            this.mHandler.sendHit(paramsToSend);
        } else {
            Log.w("Too many hits sent too quickly, rate limiting invoked.");
        }
    }

    public String get(String key) {
        GAUsage.getInstance().setUsage(GAUsage.Field.GET);
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        if (this.mParams.containsKey(key)) {
            return this.mParams.get(key);
        }
        if (key.equals(Fields.LANGUAGE)) {
            return Utils.getLanguage(Locale.getDefault());
        }
        if (this.mClientIdDefaultProvider != null && this.mClientIdDefaultProvider.providesField(key)) {
            return this.mClientIdDefaultProvider.getValue(key);
        }
        if (this.mScreenResolutionDefaultProvider != null && this.mScreenResolutionDefaultProvider.providesField(key)) {
            return this.mScreenResolutionDefaultProvider.getValue(key);
        }
        if (this.mAppFieldsDefaultProvider == null || !this.mAppFieldsDefaultProvider.providesField(key)) {
            return null;
        }
        return this.mAppFieldsDefaultProvider.getValue(key);
    }

    public void set(String key, String value) {
        GAUsage.getInstance().setUsage(GAUsage.Field.SET);
        if (value == null) {
            this.mParams.remove(key);
        } else {
            this.mParams.put(key, value);
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setLastTrackTime(long lastTrackTime) {
        this.mLastTrackTime = lastTrackTime;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setTokens(long tokens) {
        this.mTokens = tokens;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized boolean tokensAvailable() {
        boolean z;
        long timeNow = System.currentTimeMillis();
        if (this.mTokens < MAX_TOKENS) {
            long timeElapsed = timeNow - this.mLastTrackTime;
            if (timeElapsed > 0) {
                this.mTokens = Math.min(MAX_TOKENS, this.mTokens + timeElapsed);
            }
        }
        this.mLastTrackTime = timeNow;
        if (this.mTokens >= 2000) {
            this.mTokens -= 2000;
            z = true;
        } else {
            Log.w("Excessive tracking detected.  Tracking call ignored.");
            z = false;
        }
        return z;
    }
}
