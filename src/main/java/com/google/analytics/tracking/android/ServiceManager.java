package com.google.analytics.tracking.android;

public abstract class ServiceManager {
    @Deprecated
    public abstract void dispatchLocalHits();

    /* access modifiers changed from: package-private */
    public abstract void onRadioPowered();

    @Deprecated
    public abstract void setForceLocalDispatch();

    @Deprecated
    public abstract void setLocalDispatchPeriod(int i);

    /* access modifiers changed from: package-private */
    public abstract void updateConnectivityStatus(boolean z);
}
