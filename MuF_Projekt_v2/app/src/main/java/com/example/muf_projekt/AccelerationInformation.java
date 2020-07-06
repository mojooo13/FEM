package com.example.muf_projekt;

import android.hardware.Sensor;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.github.mikephil.charting.data.LineData;

import java.util.UUID;

@Entity(tableName = "measurement_DB")
public class AccelerationInformation {


    @PrimaryKey
    @NonNull
    private String id;

    private float timestamp;

    private float x;

    private float y;
    private float z;

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public float getTimestamp() {
        return timestamp;
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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AccelerationInformation(float x, float y, float z){
        this.id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = 0;
    }

    public AccelerationInformation(){

    }
}
