package com.google.zxing.client.android.result;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import com.google.zxing.client.android.wifi.WifiConfigManager;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.WifiParsedResult;
import jp.co.cybird.barcodekanojoForGAM.R;

public final class WifiResultHandler extends ResultHandler {
    private final CaptureActivity parent;
    private final AsyncTaskExecInterface taskExec = ((AsyncTaskExecInterface) new AsyncTaskExecManager().build());

    public WifiResultHandler(CaptureActivity activity, ParsedResult result) {
        super(activity, result);
        this.parent = activity;
    }

    public int getButtonCount() {
        return 1;
    }

    public int getButtonText(int index) {
        return R.string.button_wifi;
    }

    public void handleButtonPress(int index) {
        if (index == 0) {
            final Activity activity = getActivity();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), R.string.wifi_changing_network, 0).show();
                }
            });
            this.taskExec.execute(new WifiConfigManager((WifiManager) getActivity().getSystemService("wifi")), (WifiParsedResult) getResult());
            this.parent.restartPreviewAfterDelay(0);
        }
    }

    public CharSequence getDisplayContents() {
        WifiParsedResult wifiResult = (WifiParsedResult) getResult();
        StringBuilder contents = new StringBuilder(50);
        ParsedResult.maybeAppend(String.valueOf(this.parent.getString(R.string.wifi_ssid_label)) + 10 + wifiResult.getSsid(), contents);
        ParsedResult.maybeAppend(String.valueOf(this.parent.getString(R.string.wifi_type_label)) + 10 + wifiResult.getNetworkEncryption(), contents);
        return contents.toString();
    }

    public int getDisplayTitle() {
        return R.string.result_wifi;
    }
}
