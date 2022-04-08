package jp.co.cybird.barcodekanojoForGAM.push;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import jp.co.cybird.android.lib.gcm.Const;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.barcodekanojoForGAM.R;

public class BarcodeService extends IntentService {
	//TODO: This and Notifications in general
	private static final String EXTRA_TOKEN = "token";
	private static final Object LOCK = BarcodeService.class;
	private static final int MAX_BACKOFF_MS = ((int) TimeUnit.SECONDS.toMillis(3600));
	private static final String TAG = "BarcodeService";
	private static final Random sRandom = new Random();
	private static final String TOKEN = Long.toBinaryString(sRandom.nextLong());
	private static int sCounter = 0;
	private static PowerManager.WakeLock sWakeLock;
	private final String[] mSenderIds;

	protected BarcodeService() {
		this(getName("DynamicSenderIds"), null);
	}

	/*protected BarcodeService(String... senderIds) {
		this(getName(senderIds), senderIds);
	}*/

	private BarcodeService(String name, String[] senderIds) {
		super(name);
		this.mSenderIds = senderIds;
	}

	private static String getName(String senderId) {
		StringBuilder append = new StringBuilder().append("GCMIntentService-").append(senderId).append("-");
		int i = sCounter + 1;
		sCounter = i;
		String name = append.append(i).toString();
		Log.v(TAG, "Intent service name: " + name);
		return name;
	}

	private static String getName(String[] senderIds) {
		return null;//getName(GCMRegistrar.getFlatSenderIds(senderIds));
	}

	protected String[] getSenderIds(Context context) {
		if (this.mSenderIds != null) {
			return this.mSenderIds;
		}
		throw new IllegalStateException("sender id not set on constructor");
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

	protected void onDeletedMessages(Context context, int total) {
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		return true;
	}

	@Override
	protected final void onHandleIntent(Intent intent) {
		String sTotal = null;
		try {
			Context context = getApplicationContext();
			String action = intent.getAction();
			if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
				//GCMRegistrar.setRetryBroadcastReceiver(context);
				//handleRegistration(context, intent);
			} else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
				String messageType = intent.getStringExtra("message_type");
				if (messageType == null) {
					onMessage(context, intent);
				} else if (messageType.equals("deleted_messages")) {
					sTotal = intent.getStringExtra("total_deleted");
					if (sTotal != null) {
						int total = Integer.parseInt(sTotal);
						Log.v(TAG, "Received deleted messages notification: " + total);
						onDeletedMessages(context, total);
					}
				} else {
					Log.e(TAG, "Received unknown special message: " + messageType);
				}
			} else if (action.equals("com.google.android.gcm.intent.RETRY")) {
				String token = intent.getStringExtra(EXTRA_TOKEN);
				if (!TOKEN.equals(token)) {
					Log.e(TAG, "Received invalid token: " + token);
					synchronized (LOCK) {
						if (sWakeLock != null) {
							Log.v(TAG, "Releasing wakelock");
							sWakeLock.release();
						} else {
							Log.e(TAG, "Wakelock reference is null");
						}
					}
					return;
				} /*else if (GCMRegistrar.isRegistered(context)) {
					GCMRegistrar.internalUnregister(context);
				} else {
					GCMRegistrar.internalRegister(context, getSenderIds(context));
				}*/
			}
		} catch (NumberFormatException e) {
			Log.e(TAG, "GCM returned invalid number of deleted messages: " + sTotal);
		} catch (Throwable th) {
			synchronized (LOCK) {
				if (sWakeLock != null) {
					Log.v(TAG, "Releasing wakelock");
					sWakeLock.release();
				} else {
					Log.e(TAG, "Wakelock reference is null");
				}
				throw th;
			}
		}
		synchronized (LOCK) {
			if (sWakeLock != null) {
				Log.v(TAG, "Releasing wakelock");
				sWakeLock.release();
			} else {
				Log.e(TAG, "Wakelock reference is null");
			}
		}
	}

	protected void onError(Context context, String errorId) {
		DLog.d("JP.CO.CYBIRD.ANDROID.GCM", "onError() errorId: " + errorId);
	}

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

    //protected void onRegistered(Context context, String registrationId) {
	//	SharedPreferences.Editor sharedata = context.getSharedPreferences(Const.PREF_FILE_NAME, 0).edit();
	//	sharedata.putString("lib_gcm_registration_ID", registrationId);
	//	sharedata.apply();
	//	GCMUtilities.sendRegistrationInfo(context, true);
	//	Intent i = new Intent(context, BarcodePushActivity.class);
	//	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//	if (Build.VERSION.SDK_INT >= 5) {
	//		i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	//	}
    //    i.putExtra("reg_id", registrationId);
    //    i.putExtra("push_type", "registered");
    //    startActivity(i);
    //}

	//protected void onUnregistered(Context context, String registrationId) {
	//	Log.d(TAG, "onUnregistered() registrationId: " + registrationId);
	//	GCMUtilities.sendUnregistrationInfo(context);
	//	context.getSharedPreferences(Const.PREF_FILE_NAME, 0).edit().remove("lib_gcm_registration_ID").apply();
	//}

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

		Notification n;
		if (Build.VERSION.SDK_INT >= 11) {
			Notification.Builder nb = new Notification.Builder(context);
			nb.setTicker(notificationContents);
			nb.setSmallIcon(R.drawable.icon_512);
			nb.setContentTitle(getApplicationName());
			nb.setContentText(notificationContents);
			nb.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT));
			nb.setNumber(number);
			nb.setAutoCancel(true);

			SharedPreferences prefs = context.getSharedPreferences(Const.PREF_FILE_NAME, 0);
			boolean willPlaySound = prefs.getBoolean("lib_gcm_willPlaySound", false);
			int defaults = 0;
			if (prefs.getBoolean("lib_gcm_willVibrate", false)) {
				defaults |= Notification.DEFAULT_VIBRATE;
			}
			if (willPlaySound) {
				defaults |= Notification.DEFAULT_SOUND;
				if (!"".equals(snd)) {
					Log.e(TAG, "sound uri is  android.resource://" + getPackageName() + "/raw/" + snd);
					int doesSoundExist = context.getResources().getIdentifier(snd, "raw", context.getPackageName());
					Log.e(TAG, "doesSoundExist is " + doesSoundExist);
					if (doesSoundExist != 0) {
						//TODO: Should use Notification Channel for SDK 26+
						nb.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/" + snd));
					}
				}
			}
			nb.setDefaults(defaults);
			if (Build.VERSION.SDK_INT >= 16) {
				n = nb.build();
			} else {
				n = nb.getNotification();
			}
		} else {
			n = new Notification(R.drawable.icon_512, notificationContents, System.currentTimeMillis());
		}
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(nid, n);
    }

	//private void handleRegistration(Context context, Intent intent) {
	//	String registrationId = intent.getStringExtra("registration_id");
	//	String error = intent.getStringExtra("error");
	//	String unregistered = intent.getStringExtra("unregistered");
	//	Log.d(TAG, "handleRegistration: registrationId = " + registrationId + ", error = " + error + ", unregistered = " + unregistered);
	//	if (registrationId != null) {
	//		GCMRegistrar.resetBackoff(context);
	//		GCMRegistrar.setRegistrationId(context, registrationId);
	//		onRegistered(context, registrationId);
	//	} else if (unregistered != null) {
	//		GCMRegistrar.resetBackoff(context);
	//		onUnregistered(context, GCMRegistrar.clearRegistrationId(context));
	//	} else {
	//		Log.d(TAG, "Registration error: " + error);
	//		if (!"SERVICE_NOT_AVAILABLE".equals(error)) {
	//			onError(context, error);
	//		} else if (onRecoverableError(context, error)) {
	//			int backoffTimeMs = GCMRegistrar.getBackoff(context);
	//			int nextAttempt = (backoffTimeMs / 2) + sRandom.nextInt(backoffTimeMs);
	//			Log.d(TAG, "Scheduling registration retry, backoff = " + nextAttempt + " (" + backoffTimeMs + ")");
	//			Intent retryIntent = new Intent("com.google.android.gcm.intent.RETRY");
	//			retryIntent.putExtra(EXTRA_TOKEN, TOKEN);
	//			((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + ((long) nextAttempt), PendingIntent.getBroadcast(context, 0, retryIntent, 0));
	//			if (backoffTimeMs < MAX_BACKOFF_MS) {
	//				GCMRegistrar.setBackoff(context, backoffTimeMs * 2);
	//			}
	//		} else {
	//			Log.d(TAG, "Not retrying failed operation");
	//		}
	//	}
	//}
}
