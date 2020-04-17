package jp.co.cybird.android.lib.gcm;

import android.app.Activity;
import android.os.Bundle;

public class GCMIntermediateActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GCMTransfer.action(this);
    }
}
