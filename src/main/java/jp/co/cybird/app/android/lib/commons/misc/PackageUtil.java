package jp.co.cybird.app.android.lib.commons.misc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.tracker.TrackerWrapper;

public class PackageUtil {
    public static boolean isInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void launchActivity(Context context, String url) {
        String packageNameWithParam = url.substring("market://details?id=".length());
        int i = packageNameWithParam.indexOf("&");
        if (i != -1) {
            launchActivity(context, packageNameWithParam.substring(0, i), packageNameWithParam.substring(i));
        } else {
            launchActivity(context, packageNameWithParam, "");
        }
    }

    public static void launchActivity(Context context, String packageName, String param) {
        Intent launchIntent = null;
        try {
            launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (launchIntent != null) {
            TrackerWrapper.sendEvent("アプリリンク", packageName, "アプリ起動", 1);
            launchIntent.putExtra("CYReferer", context.getPackageName());
            launchIntent.putExtra("CYReferer_params", param);
            context.startActivity(launchIntent);
            return;
        }
        TrackerWrapper.sendEvent("アプリリンク", packageName, "マーケット", 1);
        goMarket(context, packageName, param);
    }

    public static void trackIfFromCYReferer(Intent i) {
        String fromPackageName = i.getStringExtra("CYReferer");
        if (fromPackageName != null) {
            i.removeExtra("CYReferer");
            TrackerWrapper.sendEvent("CYReferer", fromPackageName, i.getStringExtra("CYReferer_params"), 1);
        }
    }

    public static void goMarket(Context context, String packageName, String param) {
        if (param == null) {
            param = "";
        }
        Uri uri = Uri.parse("market://details?id=".concat(packageName).concat(param));
        DLog.d("DEBUG:goMarket", "uri: " + uri.toString());
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean goApp(Context context, String packageName, String param) {
        if (isInstalled(context, packageName)) {
            launchActivity(context, packageName, param);
            return true;
        }
        goMarket(context, packageName, param);
        return false;
    }
}
