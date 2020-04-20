package com.google.analytics.tracking.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.gms.common.util.VisibleForTesting;

class GANetworkReceiver extends BroadcastReceiver {
    @VisibleForTesting
    static final String SELF_IDENTIFYING_EXTRA = GANetworkReceiver.class.getName();
    private final ServiceManager mManager;

    GANetworkReceiver(ServiceManager manager) {
        this.mManager = manager;
    }

    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            Bundle b = intent.getExtras();
            Boolean notConnected = Boolean.FALSE;
            if (b != null) {
                notConnected = Boolean.valueOf(intent.getExtras().getBoolean("noConnectivity"));
            }
            this.mManager.updateConnectivityStatus(!notConnected.booleanValue());
        } else if ("com.google.analytics.RADIO_POWERED".equals(action) && !intent.hasExtra(SELF_IDENTIFYING_EXTRA)) {
            this.mManager.onRadioPowered();
        }
    }

    public void register(Context context) {
        IntentFilter connectivityFilter = new IntentFilter();
        connectivityFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(this, connectivityFilter);
        IntentFilter radioPoweredFilter = new IntentFilter();
        radioPoweredFilter.addAction("com.google.analytics.RADIO_POWERED");
        radioPoweredFilter.addCategory(context.getPackageName());
        context.registerReceiver(this, radioPoweredFilter);
    }

    public static void sendRadioPoweredBroadcast(Context context) {
        Intent intent = new Intent("com.google.analytics.RADIO_POWERED");
        intent.addCategory(context.getPackageName());
        intent.putExtra(SELF_IDENTIFYING_EXTRA, true);
        context.sendBroadcast(intent);
    }
}
