package jp.co.cybird.barcodekanojoForGAM.push;

import android.content.Context;
import jp.co.cybird.android.lib.gcm.CybirdGCMReceiver;

public class BarcodeGCMReceiver extends CybirdGCMReceiver {
    /* access modifiers changed from: protected */
    public String getGCMIntentServiceClassName(Context context) {
        return "jp.co.cybird.barcodekanojoForGAM.push.BarcodeService";
    }
}
