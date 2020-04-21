package com.google.tagmanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.google.analytics.tracking.android.CampaignTrackingService;
import com.google.android.gms.common.util.VisibleForTesting;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;

public final class InstallReferrerService extends IntentService {
    @VisibleForTesting
    Context contextOverride;
    @VisibleForTesting
    CampaignTrackingService gaService;

    public InstallReferrerService(String name) {
        super(name);
    }

    public InstallReferrerService() {
        super("InstallReferrerService");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        String referrer = intent.getStringExtra(AppLauncherConsts.REQUEST_PARAM_GENERAL_REFFERER);
        Context context = this.contextOverride != null ? this.contextOverride : getApplicationContext();
        InstallReferrerUtil.saveInstallReferrer(context, referrer);
        forwardToGoogleAnalytics(context, intent);
    }

    private void forwardToGoogleAnalytics(Context context, Intent intent) {
        if (this.gaService == null) {
            this.gaService = new CampaignTrackingService();
        }
        this.gaService.processIntent(context, intent);
    }
}
