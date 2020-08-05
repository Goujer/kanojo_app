package jp.co.cybird.barcodekanojoForGAM.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;

public class MediaMountReceiver extends BroadcastReceiver {

    private static final String TAG = "MediaMountReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.MEDIA_EJECT")) {
            context.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
        }
    }
}
