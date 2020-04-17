package net.nend.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;

final class NendHelper {
    private static boolean mDebuggable = false;
    private static boolean mDev = false;

    private NendHelper() {
    }

    static void setDebuggable(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (info.metaData != null) {
                mDebuggable = info.metaData.getBoolean("NendDebuggable", false);
                mDev = info.metaData.getBoolean("NendDev", false);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    static void disableDebug() {
        mDebuggable = false;
        mDev = false;
    }

    static boolean isDebuggable() {
        return mDebuggable;
    }

    static boolean isDev() {
        return mDev;
    }

    static boolean isConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    static String md5String(String str) {
        byte[] mdbytes = new byte[16];
        try {
            MessageDigest digest = MessageDigest.getInstance(Digest.MD5);
            digest.update(str.getBytes());
            mdbytes = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            NendLog.e("nend_SDK", e.getMessage(), e);
        }
        StringBuffer sb = new StringBuffer();
        for (byte b : mdbytes) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    static void startBrowser(View view, String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (hasImplicitIntent(view.getContext(), intent)) {
            intent.setFlags(268435456);
            view.getContext().startActivity(intent);
        }
    }

    private static boolean hasImplicitIntent(Context context, Intent intent) {
        if (context.getPackageManager().queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED).size() > 0) {
            return true;
        }
        return false;
    }
}
