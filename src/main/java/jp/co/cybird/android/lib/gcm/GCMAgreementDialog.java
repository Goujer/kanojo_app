package jp.co.cybird.android.lib.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import jp.co.cybird.app.android.lib.commons.dialog.BaseAgreementDialog;

public class GCMAgreementDialog extends BaseAgreementDialog {
    public GCMAgreementDialog(Context context, int eulaVersion, String eulaUrl, String prefKey) {
        super(context, eulaVersion, eulaUrl, prefKey, null, null, null);
    }

    protected void handleDecline() {
        super.handleDecline();
        SharedPreferences.Editor e = this.mPref.edit();
        e.putBoolean("lib_gcm_willSendNotification", false);
        e.commit();
        super.saveAgreement();
    }

    protected void handleAgree() {
        super.handleAgree();
        SharedPreferences.Editor e = this.mPref.edit();
        e.putBoolean("lib_gcm_willSendNotification", true);
        e.putBoolean("lib_gcm_willPlaySound", true);
        e.putBoolean("lib_gcm_willVibrate", true);
        e.commit();
    }
}
