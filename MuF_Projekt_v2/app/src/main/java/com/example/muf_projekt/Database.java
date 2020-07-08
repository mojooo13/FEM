package com.example.muf_projekt;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {AccelerationInformation.class}, version = 1)

public abstract class Database extends RoomDatabase {

    public abstract Dao getDatapointTable();

}


