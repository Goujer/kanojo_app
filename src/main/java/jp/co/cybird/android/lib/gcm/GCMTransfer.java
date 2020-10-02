package jp.co.cybird.android.lib.gcm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gcm.GCMConstants;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.tracker.TrackerWrapper;

public class GCMTransfer {
    public static void action(Activity activity) {
        DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "GCMIntermediateActivity.onCreate()");
        String act = "";
        String url = "";
        Intent i = new Intent();
        Boolean isActionValid = true;
        Bundle extras = activity.getIntent().getExtras();
        TrackerWrapper.init(activity);
        if (activity.getSharedPreferences(Const.PREF_FILE_NAME, 0).getBoolean("lib_gcm_is_normal", true)) {
            if (extras.containsKey("act")) {
                act = extras.getString("act");
            }
            if (extras.containsKey("url")) {
                url = extras.getString("url");
            }
            if (act.toLowerCase().equals("none")) {
                isActionValid = false;
                TrackerWrapper.sendEvent(Const.GA_NOTIFICATION_CATEGORY, Const.GA_NOTIFICATION_ACTION_TAP, Const.GA_NOTIFICATION_NONE_LABEL, 1);
            } else if ((!act.toLowerCase().equals("market") || !url.startsWith("market")) && (!act.toLowerCase().equals("browser") || !url.startsWith("http"))) {
                if (!act.toLowerCase().equals(GCMConstants.EXTRA_APPLICATION_PENDING_INTENT) || !url.startsWith("app://")) {
                    url = "app://" + activity.getPackageName() + "/";
                }
                TrackerWrapper.sendEvent(Const.GA_NOTIFICATION_CATEGORY, Const.GA_NOTIFICATION_ACTION_TAP, Const.GA_NOTIFICATION_APP_LABEL, 1);
                Uri uri = Uri.parse(url);
                Intent tempIntent = new Intent("android.intent.action.VIEW");
                tempIntent.addCategory("android.intent.category.DEFAULT");
                tempIntent.setData(uri);
                if (activity.getPackageManager().queryIntentActivities(tempIntent, 0x00010000).size() > 0) {
                    i = tempIntent;
                    i.setFlags(0x10000000);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("push", true);
                    i.putExtras(bundle);
                } else {
                    isActionValid = false;
                }
            } else {
                i = new Intent("android.intent.action.VIEW", Uri.parse(url));
                if (url.startsWith("http")) {
                    TrackerWrapper.sendEvent(Const.GA_NOTIFICATION_CATEGORY, Const.GA_NOTIFICATION_ACTION_TAP, Const.GA_NOTIFICATION_URL_LABEL, 1);
                } else {
                    TrackerWrapper.sendEvent(Const.GA_NOTIFICATION_CATEGORY, Const.GA_NOTIFICATION_ACTION_TAP, Const.GA_NOTIFICATION_MARKET_LABEL, 1);
                }
            }
        } else {
            Uri uri2 = Uri.parse("app://" + activity.getPackageName() + "/");
            Intent tempIntent2 = new Intent("android.intent.action.VIEW");
            tempIntent2.addCategory("android.intent.category.DEFAULT");
            tempIntent2.setData(uri2);
            i = tempIntent2;
            i.setFlags(0x10000000);
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("push", true);
            i.putExtra("data", extras);
            i.putExtras(bundle2);
        }
        if (extras != null && isActionValid.booleanValue()) {
            activity.startActivity(i);
        }
        activity.finish();
    }
}
