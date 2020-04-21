package com.google.tagmanager;

import android.content.Context;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

class HitSendingThreadImpl extends Thread implements HitSendingThread {
    private static HitSendingThreadImpl sInstance;
    private volatile boolean mClosed = false;
    /* access modifiers changed from: private */
    public final Context mContext;
    private volatile boolean mDisabled = false;
    /* access modifiers changed from: private */
    public volatile HitStore mUrlStore;
    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    static HitSendingThreadImpl getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new HitSendingThreadImpl(ctx);
        }
        return sInstance;
    }

    private HitSendingThreadImpl(Context ctx) {
        super("GAThread");
        if (ctx != null) {
            this.mContext = ctx.getApplicationContext();
        } else {
            this.mContext = ctx;
        }
        start();
    }

    @VisibleForTesting
    HitSendingThreadImpl(Context ctx, HitStore store) {
        super("GAThread");
        if (ctx != null) {
            this.mContext = ctx.getApplicationContext();
        } else {
            this.mContext = ctx;
        }
        this.mUrlStore = store;
        start();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public HitStore getStore() {
        return this.mUrlStore;
    }

    public void sendHit(String url) {
        sendHit(url, System.currentTimeMillis());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void sendHit(String url, long hitTime) {
        final long j = hitTime;
        final String str = url;
        queueToThread(new Runnable() {
            public void run() {
                if (HitSendingThreadImpl.this.mUrlStore == null) {
                    ServiceManagerImpl instance = ServiceManagerImpl.getInstance();
                    instance.initialize(HitSendingThreadImpl.this.mContext, this);
                    HitStore unused = HitSendingThreadImpl.this.mUrlStore = instance.getStore();
                }
                HitSendingThreadImpl.this.mUrlStore.putHit(j, str);
            }
        });
    }

    public void queueToThread(Runnable r) {
        this.queue.add(r);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public int getQueueSize() {
        return this.queue.size();
    }

    private String printStackTrace(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        t.printStackTrace(stream);
        stream.flush();
        return new String(baos.toByteArray());
    }

    public void run() {
        while (!this.mClosed) {
            try {
                Runnable r = this.queue.take();
                if (!this.mDisabled) {
                    r.run();
                }
            } catch (InterruptedException e) {
                Log.i(e.toString());
            } catch (Throwable t) {
                Log.e("Error on GAThread: " + printStackTrace(t));
                Log.e("Google Analytics is shutting down.");
                this.mDisabled = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void close() {
        this.mClosed = true;
        interrupt();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isDisabled() {
        return this.mDisabled;
    }
}
