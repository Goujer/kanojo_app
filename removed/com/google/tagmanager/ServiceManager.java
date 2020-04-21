package com.google.tagmanager;

abstract class ServiceManager {
    /* access modifiers changed from: package-private */
    public abstract void dispatch();

    /* access modifiers changed from: package-private */
    public abstract void onRadioPowered();

    /* access modifiers changed from: package-private */
    public abstract void setDispatchPeriod(int i);

    /* access modifiers changed from: package-private */
    public abstract void updateConnectivityStatus(boolean z);

    ServiceManager() {
    }
}
