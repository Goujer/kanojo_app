package jp.co.cybird.android.lib.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gcm.GCMBaseIntentService;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.security.popgate.Codec;
import jp.co.cybird.app.android.lib.commons.tracker.TrackerWrapper;
import jp.co.cybird.app.android.lib.unity.GCMProxyActivity;

public class GCMIntentService extends GCMBaseIntentService {
    /* access modifiers changed from: protected */
    public String[] getSenderIds(Context context) {
        return new String[]{GCMUtilities.getSendID(this)};
    }

    public String getApplicationName() {
        ApplicationInfo ai;
        CharSequence charSequence;
        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            ai = pm.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            ai = null;
        }
        if (ai != null) {
            charSequence = pm.getApplicationLabel(ai);
        } else {
            charSequence = "(unknown)";
        }
        return (String) charSequence;
    }

    public void addNotification(Context context, Bundle extras) {
        Intent i;
        String message = "";
        String snd = "";
        int nid = (int) (System.currentTimeMillis() % 2147483647L);
        DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "Init nid:" + nid);
        int number = 0;
        if (extras.containsKey("msg")) {
            message = extras.getString("msg");
        }
        if (extras.containsKey("num")) {
            number = Integer.parseInt(extras.getString("num"));
        }
        if (extras.containsKey("nid")) {
            nid = Integer.parseInt(extras.getString("nid"));
        }
        if (extras.containsKey("snd")) {
            snd = extras.getString("snd");
        }
        String notificationTitle = getApplicationName();
        String notificationContents = message;
        getApplicationContext();
        NotificationManager nm = (NotificationManager) getSystemService("notification");
        Notification n = new Notification(getApplicationContext().getApplicationInfo().icon, message, System.currentTimeMillis());
        n.flags = 16;
        n.number = number;
        new Intent();
        Boolean isActionValid = true;
        if (GCMUtilities.isUnity(context)) {
            i = new Intent(context, GCMProxyActivity.class);
        } else {
            i = new Intent(context, GCMIntermediateActivity.class);
        }
        i.putExtras(extras);
        i.putExtra("push", true);
        if (isActionValid.booleanValue()) {
            n.setLatestEventInfo(getApplicationContext(), notificationTitle, notificationContents, PendingIntent.getActivity(getApplicationContext(), 0, i, 134217728));
            SharedPreferences prefs = context.getSharedPreferences(Const.PREF_FILE_NAME, 0);
            boolean willPlaySound = prefs.getBoolean("lib_gcm_willPlaySound", false);
            if (prefs.getBoolean("lib_gcm_willVibrate", false)) {
                n.defaults |= 2;
            }
            if (willPlaySound) {
                n.defaults |= 1;
                if (!"".equals(snd)) {
                    DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "sound uri is  android.resource://" + getPackageName() + "/raw/" + snd);
                    int doesSoundExist = context.getResources().getIdentifier(snd, "raw", context.getPackageName());
                    DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "doesSoundExist is " + doesSoundExist);
                    if (doesSoundExist != 0) {
                        n.sound = Uri.parse("android.resource://" + getPackageName() + "/raw/" + snd);
                    }
                }
            }
            TrackerWrapper.init(getApplicationContext());
            TrackerWrapper.sendEvent(Const.GA_NOTIFICATION_CATEGORY, Const.GA_NOTIFICATION_ACTION_DISPLAY, Const.GA_NOTIFICATION_DISPLAY_LABEL, 1);
            nm.notify(nid, n);
        }
    }

    /* access modifiers changed from: protected */
    public void onError(Context context, String errorId) {
        DLog.d("JP.CO.CYBIRD.ANDROID.GCM", "onError() errorId: " + errorId);
    }

    /* access modifiers changed from: protected */
    public void onMessage(Context context, Intent intent) {
        if (context.getSharedPreferences(Const.PREF_FILE_NAME, 0).getBoolean("lib_gcm_willSendNotification", true)) {
            addNotification(context, intent.getExtras());
        } else {
            GCMUtilities.unregisterGCM(context);
        }
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context context, String registrationId) {
        SharedPreferences.Editor sharedata = context.getSharedPreferences(Const.PREF_FILE_NAME, 0).edit();
        sharedata.putString("lib_gcm_registration_ID", Codec.encode(registrationId));
        sharedata.commit();
        GCMUtilities.sendRegistrationInfo(context, true);
    }

    /* access modifiers changed from: protected */
    public void onUnregistered(Context context, String registrationId) {
        DLog.d("JP.CO.CYBIRD.ANDROID.GCM", "onUnregistered() registrationId: " + registrationId);
        GCMUtilities.sendUnregistrationInfo(context);
        context.getSharedPreferences(Const.PREF_FILE_NAME, 0).edit().remove("lib_gcm_registration_ID").commit();
    }
}
