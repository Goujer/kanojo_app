package com.google.zxing.client.android.camera.open;

import android.hardware.Camera;

final class DefaultOpenCameraInterface implements OpenCameraInterface {
    DefaultOpenCameraInterface() {
    }

    public Camera open() {
        return Camera.open();
    }
}
