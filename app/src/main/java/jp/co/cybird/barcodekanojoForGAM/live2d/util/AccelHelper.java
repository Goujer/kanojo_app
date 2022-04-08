package jp.co.cybird.barcodekanojoForGAM.live2d.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelHelper {
    private final Sensor accelerometer;
    private AccelListener listener;
    private MySensorListener sensorListener = new MySensorListener(this, null);
    private SensorManager sensorManager;

    public interface AccelListener {
        void accelUpdated(float f, float f2, float f3);
    }

    public AccelHelper(Context a) {
        this.sensorManager = (SensorManager) a.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
    }

    public void start() {
        try {
            this.sensorManager.registerListener(this.sensorListener, this.accelerometer, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.sensorManager.unregisterListener(this.sensorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MySensorListener implements SensorEventListener {
        private MySensorListener() {
        }

        /* synthetic */ MySensorListener(AccelHelper accelHelper, MySensorListener mySensorListener) {
            this();
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent e) {
            if (e.sensor.getType() == 1 && AccelHelper.this.listener != null) {
                AccelHelper.this.listener.accelUpdated((-e.values[0]) / 9.80665f, (-e.values[1]) / 9.80665f, (-e.values[2]) / 9.80665f);
            }
        }
    }

    public void setAccelListener(AccelListener lis) {
        this.listener = lis;
    }
}
