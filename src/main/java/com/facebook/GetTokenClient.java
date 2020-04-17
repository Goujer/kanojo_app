package com.facebook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.accessibility.AccessibilityEventCompat;

final class GetTokenClient implements ServiceConnection {
    final String applicationId;
    final Context context;
    final Handler handler;
    CompletedListener listener;
    boolean running;
    Messenger sender;

    interface CompletedListener {
        void completed(Bundle bundle);
    }

    GetTokenClient(Context context2, String applicationId2) {
        Context applicationContext = context2.getApplicationContext();
        this.context = applicationContext == null ? context2 : applicationContext;
        this.applicationId = applicationId2;
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                GetTokenClient.this.handleMessage(message);
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void setCompletedListener(CompletedListener listener2) {
        this.listener = listener2;
    }

    /* access modifiers changed from: package-private */
    public boolean start() {
        Intent intent = new Intent("com.facebook.platform.PLATFORM_SERVICE");
        intent.addCategory("android.intent.category.DEFAULT");
        Intent intent2 = NativeProtocol.validateKatanaServiceIntent(this.context, intent);
        if (intent2 == null) {
            callback((Bundle) null);
            return false;
        }
        this.running = true;
        this.context.bindService(intent2, this, 1);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        this.running = false;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        this.sender = new Messenger(service);
        getToken();
    }

    public void onServiceDisconnected(ComponentName name) {
        this.sender = null;
        this.context.unbindService(this);
        callback((Bundle) null);
    }

    private void getToken() {
        Bundle data = new Bundle();
        data.putString("com.facebook.platform.extra.APPLICATION_ID", this.applicationId);
        Message request = Message.obtain((Handler) null, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        request.arg1 = 20121101;
        request.setData(data);
        request.replyTo = new Messenger(this.handler);
        try {
            this.sender.send(request);
        } catch (RemoteException e) {
            callback((Bundle) null);
        }
    }

    /* access modifiers changed from: private */
    public void handleMessage(Message message) {
        if (message.what == 65537) {
            Bundle extras = message.getData();
            if (extras.getString("com.facebook.platform.status.ERROR_TYPE") != null) {
                callback((Bundle) null);
            } else {
                callback(extras);
            }
            this.context.unbindService(this);
        }
    }

    private void callback(Bundle result) {
        if (this.running) {
            this.running = false;
            CompletedListener callback = this.listener;
            if (callback != null) {
                callback.completed(result);
            }
        }
    }
}
