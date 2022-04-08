package com.google.zxing.client.android;

import android.app.Activity;
import android.content.DialogInterface;

public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish2) {
        this.activityToFinish = activityToFinish2;
    }

    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    private void run() {
        this.activityToFinish.finish();
    }
}
