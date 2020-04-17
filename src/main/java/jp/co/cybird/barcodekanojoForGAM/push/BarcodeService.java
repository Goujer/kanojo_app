package jp.co.cybird.barcodekanojoForGAM.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.google.analytics.tracking.android.Tracker;
import jp.co.cybird.android.lib.gcm.Const;
import jp.co.cybird.android.lib.gcm.GCMIntentService;
import jp.co.cybird.app.android.lib.commons.log.DLog;

public class BarcodeService extends GCMIntentService {
    private Tracker tracker;

    /* access modifiers changed from: protected */
    public void onMessage(Context context, Intent intent) {
        Bundle extra = intent.getExtras();
        if (extra != null) {
            for (String entry : extra.keySet()) {
                Log.d("BarcodeService: ", entry);
                Log.d("BarcodeService val: ", extra.get(entry).toString());
            }
        }
        generateNotifications(context, intent.getExtras());
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context context, String registrationId) {
        super.onRegistered(context, registrationId);
        Intent i = new Intent(context, BarcodePushActivity.class);
        i.addFlags(268435456);
        i.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        i.putExtra("reg_id", registrationId);
        i.putExtra("push_type", "registered");
        startActivity(i);
    }

    public void generateNotifications(Context context, Bundle extras) {
        String message = "";
        int number = 0;
        int nid = 0;
        String snd = "";
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
        NotificationManager nm = (NotificationManager) getSystemService("notification");
        Notification n = new Notification(getApplicationContext().getApplicationInfo().icon, message, System.currentTimeMillis());
        n.flags = 16;
        n.number = number;
        new Intent();
        Boolean isActionValid = true;
        Intent i = new Intent(context, BarcodePushActivity.class);
        i.putExtra("push_type", "notification");
        i.putExtras(extras);
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
            nm.notify(nid, n);
        }
    }
}
