package com.google.tagmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;

public final class InstallReferrerReceiver extends BroadcastReceiver {
    static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra(AppLauncherConsts.REQUEST_PARAM_GENERAL_REFFERER);
        if (INSTALL_ACTION.equals(intent.getAction()) && referrer != null) {
            InstallReferrerUtil.cacheInstallReferrer(referrer);
            Intent serviceIntent = new Intent(context, InstallReferrerService.class);
            serviceIntent.putExtra(AppLauncherConsts.REQUEST_PARAM_GENERAL_REFFERER, referrer);
            context.startService(serviceIntent);
        }
    }
}
