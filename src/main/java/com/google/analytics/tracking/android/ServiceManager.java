package com.google.analytics.tracking.android;

public abstract class ServiceManager {
    @Deprecated
    public abstract void dispatchLocalHits();

    abstract void onRadioPowered();

    @Deprecated
    public abstract void setForceLocalDispatch();

    @Deprecated
    public abstract void setLocalDispatchPeriod(int i);

    abstract void updateConnectivityStatus(boolean z);
}
