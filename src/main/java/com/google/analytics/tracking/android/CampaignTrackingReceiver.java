package com.google.analytics.tracking.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class CampaignTrackingReceiver extends BroadcastReceiver {
    static final String CAMPAIGN_KEY = "referrer";
    static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    public void onReceive(Context ctx, Intent intent) {
        String campaign = intent.getStringExtra("referrer");
        if (INSTALL_ACTION.equals(intent.getAction()) && campaign != null) {
            Intent serviceIntent = new Intent(ctx, CampaignTrackingService.class);
            serviceIntent.putExtra("referrer", campaign);
            ctx.startService(serviceIntent);
        }
    }
}
