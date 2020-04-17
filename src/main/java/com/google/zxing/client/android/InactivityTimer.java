package com.google.zxing.client.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;

final class InactivityTimer {
    private static final long INACTIVITY_DELAY_MS = 300000;
    /* access modifiers changed from: private */
    public static final String TAG = InactivityTimer.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Activity activity;
    private InactivityAsyncTask inactivityTask;
    private final BroadcastReceiver powerStatusReceiver = new PowerStatusReceiver(this, (PowerStatusReceiver) null);
    private final AsyncTaskExecInterface taskExec = ((AsyncTaskExecInterface) new AsyncTaskExecManager().build());

    InactivityTimer(Activity activity2) {
        this.activity = activity2;
        onActivity();
    }

    /* access modifiers changed from: package-private */
    public synchronized void onActivity() {
        cancel();
        this.inactivityTask = new InactivityAsyncTask(this, (InactivityAsyncTask) null);
        this.taskExec.execute(this.inactivityTask, new Object[0]);
    }

    public void onPause() {
        cancel();
        this.activity.unregisterReceiver(this.powerStatusReceiver);
    }

    public void onResume() {
        this.activity.registerReceiver(this.powerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        onActivity();
    }

    /* access modifiers changed from: private */
    public synchronized void cancel() {
        AsyncTask<?, ?, ?> task = this.inactivityTask;
        if (task != null) {
            task.cancel(true);
            this.inactivityTask = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void shutdown() {
        cancel();
    }

    private final class PowerStatusReceiver extends BroadcastReceiver {
        private PowerStatusReceiver() {
        }

        /* synthetic */ PowerStatusReceiver(InactivityTimer inactivityTimer, PowerStatusReceiver powerStatusReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                if (intent.getIntExtra("plugged", -1) <= 0) {
                    InactivityTimer.this.onActivity();
                } else {
                    InactivityTimer.this.cancel();
                }
            }
        }
    }

    private final class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {
        private InactivityAsyncTask() {
        }

        /* synthetic */ InactivityAsyncTask(InactivityTimer inactivityTimer, InactivityAsyncTask inactivityAsyncTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public Object doInBackground(Object... objects) {
            try {
                Thread.sleep(300000);
                Log.i(InactivityTimer.TAG, "Finishing activity due to inactivity");
                InactivityTimer.this.activity.finish();
                return null;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}
