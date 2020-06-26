package com.example.muf_projekt;

import android.hardware.Sensor;

import com.github.mikephil.charting.data.LineData;

public class AccelerationInformation {
    private Sensor sensor;
    private float x;
    private float y;
    private float z;
    private float timestamp;
    private LineData lineData;


    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
