package com.google.zxing.client.android.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;

final class PreviewCallback implements Camera.PreviewCallback {
    private static final String TAG = PreviewCallback.class.getSimpleName();
    private final CameraConfigurationManager configManager;
    private Handler previewHandler;
    private int previewMessage;

    PreviewCallback(CameraConfigurationManager configManager2) {
        this.configManager = configManager2;
    }

    /* access modifiers changed from: package-private */
    public void setHandler(Handler previewHandler2, int previewMessage2) {
        this.previewHandler = previewHandler2;
        this.previewMessage = previewMessage2;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = this.configManager.getCameraResolution();
        Handler thePreviewHandler = this.previewHandler;
        if (cameraResolution == null || thePreviewHandler == null) {
            Log.d(TAG, "Got preview callback, but no handler or resolution available");
            return;
        }
        thePreviewHandler.obtainMessage(this.previewMessage, cameraResolution.x, cameraResolution.y, data).sendToTarget();
        this.previewHandler = null;
    }
}
