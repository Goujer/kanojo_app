package com.google.analytics.tracking.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.google.analytics.tracking.android.GAUsage;
import com.google.android.gms.common.util.VisibleForTesting;

public class GAServiceManager extends ServiceManager {
    private static final int MSG_KEY = 1;
    /* access modifiers changed from: private */
    public static final Object MSG_OBJECT = new Object();
    private static GAServiceManager instance;
    /* access modifiers changed from: private */
    public boolean connected = true;
    private Context ctx;
    /* access modifiers changed from: private */
    public int dispatchPeriodInSeconds = 1800;
    /* access modifiers changed from: private */
    public Handler handler;
    private boolean listenForNetwork = true;
    private AnalyticsStoreStateListener listener = new AnalyticsStoreStateListener() {
        public void reportStoreIsEmpty(boolean isEmpty) {
            GAServiceManager.this.updatePowerSaveMode(isEmpty, GAServiceManager.this.connected);
        }
    };
    private GANetworkReceiver networkReceiver;
    private boolean pendingDispatch = true;
    private boolean pendingForceLocalDispatch;
    private String pendingHostOverride;
    private AnalyticsStore store;
    /* access modifiers changed from: private */
    public boolean storeIsEmpty = false;
    private volatile AnalyticsThread thread;

    public static GAServiceManager getInstance() {
        if (instance == null) {
            instance = new GAServiceManager();
        }
        return instance;
    }

    private GAServiceManager() {
    }

    @VisibleForTesting
    static void clearInstance() {
        instance = null;
    }

    @VisibleForTesting
    GAServiceManager(Context ctx2, AnalyticsThread thread2, AnalyticsStore store2, boolean listenForNetwork2) {
        this.store = store2;
        this.thread = thread2;
        this.listenForNetwork = listenForNetwork2;
        initialize(ctx2, thread2);
    }

    private void initializeNetworkReceiver() {
        this.networkReceiver = new GANetworkReceiver(this);
        this.networkReceiver.register(this.ctx);
    }

    private void initializeHandler() {
        this.handler = new Handler(this.ctx.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && GAServiceManager.MSG_OBJECT.equals(msg.obj)) {
                    GAUsage.getInstance().setDisableUsage(true);
                    GAServiceManager.this.dispatchLocalHits();
                    GAUsage.getInstance().setDisableUsage(false);
                    if (GAServiceManager.this.dispatchPeriodInSeconds > 0 && !GAServiceManager.this.storeIsEmpty) {
                        GAServiceManager.this.handler.sendMessageDelayed(GAServiceManager.this.handler.obtainMessage(1, GAServiceManager.MSG_OBJECT), (long) (GAServiceManager.this.dispatchPeriodInSeconds * 1000));
                    }
                }
                return true;
            }
        });
        if (this.dispatchPeriodInSeconds > 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), (long) (this.dispatchPeriodInSeconds * 1000));
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void initialize(Context ctx2, AnalyticsThread thread2) {
        if (this.ctx == null) {
            this.ctx = ctx2.getApplicationContext();
            if (this.thread == null) {
                this.thread = thread2;
                if (this.pendingDispatch) {
                    dispatchLocalHits();
                    this.pendingDispatch = false;
                }
                if (this.pendingForceLocalDispatch) {
                    setForceLocalDispatch();
                    this.pendingForceLocalDispatch = false;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public AnalyticsStoreStateListener getListener() {
        return this.listener;
    }

    /* access modifiers changed from: package-private */
    public synchronized AnalyticsStore getStore() {
        if (this.store == null) {
            if (this.ctx == null) {
                throw new IllegalStateException("Cant get a store unless we have a context");
            }
            this.store = new PersistentAnalyticsStore(this.listener, this.ctx);
            if (this.pendingHostOverride != null) {
                this.store.getDispatcher().overrideHostUrl(this.pendingHostOverride);
                this.pendingHostOverride = null;
            }
        }
        if (this.handler == null) {
            initializeHandler();
        }
        if (this.networkReceiver == null && this.listenForNetwork) {
            initializeNetworkReceiver();
        }
        return this.store;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized void overrideHostUrl(String hostOverride) {
        if (this.store == null) {
            this.pendingHostOverride = hostOverride;
        } else {
            this.store.getDispatcher().overrideHostUrl(hostOverride);
        }
    }

    @Deprecated
    public synchronized void dispatchLocalHits() {
        if (this.thread == null) {
            Log.v("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.pendingDispatch = true;
        } else {
            GAUsage.getInstance().setUsage(GAUsage.Field.DISPATCH);
            this.thread.dispatch();
        }
    }

    @Deprecated
    public synchronized void setLocalDispatchPeriod(int dispatchPeriodInSeconds2) {
        if (this.handler == null) {
            Log.v("Dispatch period set with null handler. Dispatch will run once initialization is complete.");
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds2;
        } else {
            GAUsage.getInstance().setUsage(GAUsage.Field.SET_DISPATCH_PERIOD);
            if (!this.storeIsEmpty && this.connected && this.dispatchPeriodInSeconds > 0) {
                this.handler.removeMessages(1, MSG_OBJECT);
            }
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds2;
            if (dispatchPeriodInSeconds2 > 0 && !this.storeIsEmpty && this.connected) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), (long) (dispatchPeriodInSeconds2 * 1000));
            }
        }
    }

    @Deprecated
    public void setForceLocalDispatch() {
        if (this.thread == null) {
            Log.v("setForceLocalDispatch() queued. It will be called once initialization is complete.");
            this.pendingForceLocalDispatch = true;
            return;
        }
        GAUsage.getInstance().setUsage(GAUsage.Field.SET_FORCE_LOCAL_DISPATCH);
        this.thread.setForceLocalDispatch();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized void updatePowerSaveMode(boolean storeIsEmpty2, boolean connected2) {
        if (!(this.storeIsEmpty == storeIsEmpty2 && this.connected == connected2)) {
            if (storeIsEmpty2 || !connected2) {
                if (this.dispatchPeriodInSeconds > 0) {
                    this.handler.removeMessages(1, MSG_OBJECT);
                }
            }
            if (!storeIsEmpty2 && connected2 && this.dispatchPeriodInSeconds > 0) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), (long) (this.dispatchPeriodInSeconds * 1000));
            }
            Log.v("PowerSaveMode " + ((storeIsEmpty2 || !connected2) ? "initiated." : "terminated."));
            this.storeIsEmpty = storeIsEmpty2;
            this.connected = connected2;
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateConnectivityStatus(boolean connected2) {
        updatePowerSaveMode(this.storeIsEmpty, connected2);
    }

    /* access modifiers changed from: package-private */
    public synchronized void onRadioPowered() {
        if (!this.storeIsEmpty && this.connected && this.dispatchPeriodInSeconds > 0) {
            this.handler.removeMessages(1, MSG_OBJECT);
            this.handler.sendMessage(this.handler.obtainMessage(1, MSG_OBJECT));
        }
    }
}
