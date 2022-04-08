package jp.co.cybird.barcodekanojoForGAM.billing.util;

import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(805306394, "WakeLock");
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
