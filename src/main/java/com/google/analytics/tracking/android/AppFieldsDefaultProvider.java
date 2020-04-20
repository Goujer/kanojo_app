package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.common.util.VisibleForTesting;

class AppFieldsDefaultProvider implements DefaultProvider {
    private static AppFieldsDefaultProvider sInstance;
    private static Object sInstanceLock = new Object();
    protected String mAppId;
    protected String mAppInstallerId;
    protected String mAppName;
    protected String mAppVersion;

    public static void initializeProvider(Context c) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new AppFieldsDefaultProvider(c);
            }
        }
    }

    @VisibleForTesting
    static void dropInstance() {
        synchronized (sInstanceLock) {
            sInstance = null;
        }
    }

    public static AppFieldsDefaultProvider getProvider() {
        return sInstance;
    }

    private AppFieldsDefaultProvider(Context c) {
        PackageManager pm = c.getPackageManager();
        this.mAppId = c.getPackageName();
        this.mAppInstallerId = pm.getInstallerPackageName(this.mAppId);
        String appName = this.mAppId;
        String appVersion = null;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(c.getPackageName(), 0);
            if (packageInfo != null) {
                appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString();
                appVersion = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Error retrieving package info: appName set to " + appName);
        }
        this.mAppName = appName;
        this.mAppVersion = appVersion;
    }

    @VisibleForTesting
    protected AppFieldsDefaultProvider() {
    }

    public boolean providesField(String field) {
        return Fields.APP_NAME.equals(field) || Fields.APP_VERSION.equals(field) || Fields.APP_ID.equals(field) || Fields.APP_INSTALLER_ID.equals(field);
    }

    public String getValue(String field) {
        if (field == null) {
            return null;
        }
        if (field.equals(Fields.APP_NAME)) {
            return this.mAppName;
        }
        if (field.equals(Fields.APP_VERSION)) {
            return this.mAppVersion;
        }
        if (field.equals(Fields.APP_ID)) {
            return this.mAppId;
        }
        if (field.equals(Fields.APP_INSTALLER_ID)) {
            return this.mAppInstallerId;
        }
        return null;
    }
}
