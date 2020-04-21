package com.google.tagmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.google.android.gms.common.util.VisibleForTesting;

class ServiceManagerImpl extends ServiceManager {
    private static final int MSG_KEY = 1;
    /* access modifiers changed from: private */
    public static final Object MSG_OBJECT = new Object();
    private static ServiceManagerImpl instance;
    /* access modifiers changed from: private */
    public boolean connected = true;
    private Context ctx;
    /* access modifiers changed from: private */
    public int dispatchPeriodInSeconds = 1800;
    /* access modifiers changed from: private */
    public Handler handler;
    private boolean listenForNetwork = true;
    private HitStoreStateListener listener = new HitStoreStateListener() {
        public void reportStoreIsEmpty(boolean isEmpty) {
            ServiceManagerImpl.this.updatePowerSaveMode(isEmpty, ServiceManagerImpl.this.connected);
        }
    };
    private NetworkReceiver networkReceiver;
    private boolean pendingDispatch = true;
    private boolean readyToDispatch = false;
    /* access modifiers changed from: private */
    public HitStore store;
    /* access modifiers changed from: private */
    public boolean storeIsEmpty = false;
    private volatile HitSendingThread thread;

    public static ServiceManagerImpl getInstance() {
        if (instance == null) {
            instance = new ServiceManagerImpl();
        }
        return instance;
    }

    private ServiceManagerImpl() {
    }

    @VisibleForTesting
    static void clearInstance() {
        instance = null;
    }

    @VisibleForTesting
    ServiceManagerImpl(Context ctx2, HitSendingThread thread2, HitStore store2, boolean listenForNetwork2) {
        this.store = store2;
        this.thread = thread2;
        this.listenForNetwork = listenForNetwork2;
        initialize(ctx2, thread2);
    }

    private void initializeNetworkReceiver() {
        this.networkReceiver = new NetworkReceiver(this);
        this.networkReceiver.register(this.ctx);
    }

    private void initializeHandler() {
        this.handler = new Handler(this.ctx.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && ServiceManagerImpl.MSG_OBJECT.equals(msg.obj)) {
                    ServiceManagerImpl.this.dispatch();
                    if (ServiceManagerImpl.this.dispatchPeriodInSeconds > 0 && !ServiceManagerImpl.this.storeIsEmpty) {
                        ServiceManagerImpl.this.handler.sendMessageDelayed(ServiceManagerImpl.this.handler.obtainMessage(1, ServiceManagerImpl.MSG_OBJECT), (long) (ServiceManagerImpl.this.dispatchPeriodInSeconds * 1000));
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
    public synchronized void initialize(Context ctx2, HitSendingThread thread2) {
        if (this.ctx == null) {
            this.ctx = ctx2.getApplicationContext();
            if (this.thread == null) {
                this.thread = thread2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public HitStoreStateListener getListener() {
        return this.listener;
    }

    /* access modifiers changed from: package-private */
    public synchronized HitStore getStore() {
        if (this.store == null) {
            if (this.ctx == null) {
                throw new IllegalStateException("Cant get a store unless we have a context");
            }
            this.store = new PersistentHitStore(this.listener, this.ctx);
        }
        if (this.handler == null) {
            initializeHandler();
        }
        this.readyToDispatch = true;
        if (this.pendingDispatch) {
            dispatch();
            this.pendingDispatch = false;
        }
        if (this.networkReceiver == null && this.listenForNetwork) {
            initializeNetworkReceiver();
        }
        return this.store;
    }

    public synchronized void dispatch() {
        if (!this.readyToDispatch) {
            Log.v("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.pendingDispatch = true;
        } else {
            this.thread.queueToThread(new Runnable() {
                public void run() {
                    ServiceManagerImpl.this.store.dispatch();
                }
            });
        }
    }

    public synchronized void setDispatchPeriod(int dispatchPeriodInSeconds2) {
        if (this.handler == null) {
            Log.v("Dispatch period set with null handler. Dispatch will run once initialization is complete.");
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds2;
        } else {
            if (!this.storeIsEmpty && this.connected && this.dispatchPeriodInSeconds > 0) {
                this.handler.removeMessages(1, MSG_OBJECT);
            }
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds2;
            if (dispatchPeriodInSeconds2 > 0 && !this.storeIsEmpty && this.connected) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), (long) (dispatchPeriodInSeconds2 * 1000));
            }
        }
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
