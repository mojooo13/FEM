package com.example.muf_projekt;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    final LiveData<AccelerationInformation> accelerationLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        accelerationLiveData = new AccelerationLiveData(application.getApplicationContext());
    }

    private final static class AccelerationLiveData extends LiveData<AccelerationInformation> {
        private final AccelerationInformation accelerationInformation = new AccelerationInformation();
        private SensorManager sm;
        private Sensor accelerometer;
        private Sensor gravitySensor;
        private float[] gravity;



        private SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        float[] values = removeGravity(gravity, event.values);
                        accelerationInformation.setXYZ(values[0], values[1], values[2]);
                        accelerationInformation.setSensor(event.sensor);
                        accelerationInformation.setTimestamp((new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L);
                        setValue(accelerationInformation);

                        //LineDataSet dataSet = new LineDataSet(new Entry(accelerationInformation.getTimestamp(), accelerationInformation.getX(), "Label"); // add entries to dataset
                        //LineData lineData = new LineData(dataSet);
                        //accelerationInformation.setLineData(lineData);

                        break;
                    case Sensor.TYPE_GRAVITY:
                        gravity = event.values;
                        break;
                    default:
                        break; // Ignore this case!
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        AccelerationLiveData(Context context) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            if (sm != null) {
                gravitySensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
                accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            } else {
                // should never happen
                throw new RuntimeException("He's dead Jim!");
            }
        }

        @Override
        protected void onActive() {
            super.onActive();
            sm.registerListener(listener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            sm.unregisterListener(listener);
        }

        // taken and adjusted from https://developer.android.com/guide/topics/sensors/sensors_motion#java
        private float[] removeGravity(float[] gravity, float[] values) {
            if (gravity == null) {
                return values;
            }
            final float alpha = 0.8f;
            float g[] = new float[3];
            g[0] = alpha * gravity[0] + (1 - alpha) * values[0];
            g[1] = alpha * gravity[1] + (1 - alpha) * values[1];
            g[2] = alpha * gravity[2] + (1 - alpha) * values[2];

            return new float[]{
                    values[0] - g[0],
                    values[1] - g[1],
                    values[2] - g[2]
            };
        }
    }
}
