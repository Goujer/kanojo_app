package com.google.zxing.client.android.camera.exposure;

import android.hardware.Camera;

public interface ExposureInterface {
    void setExposure(Camera.Parameters parameters, boolean z);
}
