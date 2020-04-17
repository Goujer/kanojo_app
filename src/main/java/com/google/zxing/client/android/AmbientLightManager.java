package com.google.zxing.client.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.camera.FrontLightMode;

final class AmbientLightManager implements SensorEventListener {
    private static final float BRIGHT_ENOUGH_LUX = 450.0f;
    private static final float TOO_DARK_LUX = 45.0f;
    private CameraManager cameraManager;
    private final Context context;
    private Sensor lightSensor;

    AmbientLightManager(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public void start(CameraManager cameraManager2) {
        this.cameraManager = cameraManager2;
        if (FrontLightMode.readPref(PreferenceManager.getDefaultSharedPreferences(this.context)) == FrontLightMode.AUTO) {
            SensorManager sensorManager = (SensorManager) this.context.getSystemService("sensor");
            this.lightSensor = sensorManager.getDefaultSensor(5);
            if (this.lightSensor != null) {
                sensorManager.registerListener(this, this.lightSensor, 3);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        if (this.lightSensor != null) {
            ((SensorManager) this.context.getSystemService("sensor")).unregisterListener(this);
            this.cameraManager = null;
            this.lightSensor = null;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        float ambientLightLux = sensorEvent.values[0];
        if (this.cameraManager == null) {
            return;
        }
        if (ambientLightLux <= TOO_DARK_LUX) {
            this.cameraManager.setTorch(true);
        } else if (ambientLightLux >= BRIGHT_ENOUGH_LUX) {
            this.cameraManager.setTorch(false);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
