package com.google.tagmanager;

import android.os.Build;
import com.google.android.gms.common.util.VisibleForTesting;

class NetworkClientFactory {
    NetworkClientFactory() {
    }

    public NetworkClient createNetworkClient() {
        if (getSdkVersion() < 8) {
            return new HttpNetworkClient();
        }
        return new HttpUrlConnectionNetworkClient();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
}
