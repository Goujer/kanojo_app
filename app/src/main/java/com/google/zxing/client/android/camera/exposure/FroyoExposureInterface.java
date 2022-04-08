package com.google.zxing.client.android.camera.exposure;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.util.Log;

@TargetApi(8)
public final class FroyoExposureInterface implements ExposureInterface {
    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private static final String TAG = FroyoExposureInterface.class.getSimpleName();

    public void setExposure(Camera.Parameters parameters, boolean lightOn) {
        int desiredCompensation;
        int minExposure = parameters.getMinExposureCompensation();
        int maxExposure = parameters.getMaxExposureCompensation();
        if (minExposure == 0 && maxExposure == 0) {
            Log.i(TAG, "Camera does not support exposure compensation");
            return;
        }
        float step = parameters.getExposureCompensationStep();
        if (lightOn) {
            desiredCompensation = Math.max((int) (0.0f / step), minExposure);
        } else {
            desiredCompensation = Math.min((int) (1.5f / step), maxExposure);
        }
        Log.i(TAG, "Setting exposure compensation to " + desiredCompensation + " / " + (((float) desiredCompensation) * step));
        parameters.setExposureCompensation(desiredCompensation);
    }
}
