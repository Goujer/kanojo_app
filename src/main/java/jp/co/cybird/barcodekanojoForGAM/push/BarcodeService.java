package jp.co.cybird.barcodekanojoForGAM.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.google.analytics.tracking.android.Tracker;
import jp.co.cybird.android.lib.gcm.Const;
import jp.co.cybird.android.lib.gcm.GCMIntentService;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.barcodekanojoForGAM.R;

public class BarcodeService extends GCMIntentService {
    private Tracker tracker;

    protected void onMessage(Context context, Intent intent) {
        Bundle extra = intent.getExtras();
        if (extra != null) {
            for (String entry : extra.keySet()) {
                Log.d("BarcodeService: ", entry);
                Log.d("BarcodeService val: ", extra.get(entry).toString());
            }
        }
        generateNotifications(context, intent.getExtras());
    }

    protected void onRegistered(Context context, String registrationId) {
		super.onRegistered(context, registrationId);
		Intent i = new Intent(context, BarcodePushActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 5) {
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
        i.putExtra("reg_id", registrationId);
        i.putExtra("push_type", "registered");
        startActivity(i);
    }

    public void generateNotifications(Context context, Bundle extras) {
        String notificationContents = "";
        int number = 0;
        int nid = 0;
        String snd = "";
        if (extras.containsKey("msg")) {
			notificationContents = extras.getString("msg");
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

        new Intent();

        Intent i = new Intent(context, BarcodePushActivity.class);
        i.putExtra("push_type", "notification");
        i.putExtras(extras);

		Notification.Builder nb = new Notification.Builder(context);
		nb.setTicker(notificationContents);
		if (Build.VERSION.SDK_INT >= 23) {
			nb.setLargeIcon(Icon.createWithResource(context, R.drawable.icon_512));
		}
		nb.setContentTitle(getApplicationName());
		nb.setContentText(notificationContents);
		nb.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT));
		nb.setNumber(number);
		nb.setAutoCancel(true);

		SharedPreferences prefs = context.getSharedPreferences(Const.PREF_FILE_NAME, 0);
		boolean willPlaySound = prefs.getBoolean("lib_gcm_willPlaySound", false);
		int defaults = 0;
		if (prefs.getBoolean("lib_gcm_willVibrate", false)) {
			defaults |= 2;
		}
		if (willPlaySound) {
			defaults |= 1;
			if (!"".equals(snd)) {
				DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "sound uri is  android.resource://" + getPackageName() + "/raw/" + snd);
				int doesSoundExist = context.getResources().getIdentifier(snd, "raw", context.getPackageName());
				DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "doesSoundExist is " + doesSoundExist);
				if (doesSoundExist != 0) {
					//TODO: Should use Notification Channel for SDK 26+
					nb.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/" + snd));
				}
			}
		}
		Notification n;
		if (Build.VERSION.SDK_INT >= 16) {
			n = nb.build();
		} else {
			n = nb.getNotification();
		}
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(nid, n);
    }
}
