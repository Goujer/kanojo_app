package com.google.analytics.tracking.android;

import android.content.Context;
import android.util.DisplayMetrics;
import com.google.android.gms.common.util.VisibleForTesting;

class ScreenResolutionDefaultProvider implements DefaultProvider {
    private static ScreenResolutionDefaultProvider sInstance;
    private static Object sInstanceLock = new Object();
    private final Context mContext;

    public static void initializeProvider(Context c) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new ScreenResolutionDefaultProvider(c);
            }
        }
    }

    public static ScreenResolutionDefaultProvider getProvider() {
        ScreenResolutionDefaultProvider screenResolutionDefaultProvider;
        synchronized (sInstanceLock) {
            screenResolutionDefaultProvider = sInstance;
        }
        return screenResolutionDefaultProvider;
    }

    @VisibleForTesting
    static void dropInstance() {
        synchronized (sInstanceLock) {
            sInstance = null;
        }
    }

    @VisibleForTesting
	private ScreenResolutionDefaultProvider(Context c) {
        this.mContext = c;
    }

    public boolean providesField(String field) {
        return Fields.SCREEN_RESOLUTION.equals(field);
    }

    public String getValue(String field) {
        if (field != null && field.equals(Fields.SCREEN_RESOLUTION)) {
            return getScreenResolutionString();
        }
        return null;
    }

    private String getScreenResolutionString() {
        DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        return dm.widthPixels + "x" + dm.heightPixels;
    }
}
