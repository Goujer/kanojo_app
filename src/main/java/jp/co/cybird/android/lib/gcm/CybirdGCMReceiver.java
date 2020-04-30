package jp.co.cybird.android.lib.gcm;

import android.content.Context;
import com.google.android.gcm.GCMBroadcastReceiver;

public class CybirdGCMReceiver extends GCMBroadcastReceiver {
    protected String getGCMIntentServiceClassName(Context context) {
        return "jp.co.cybird.android.lib.gcm.GCMIntentService";
    }
}
