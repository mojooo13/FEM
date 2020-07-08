package com.example.muf_projekt;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    public void insert(AccelerationInformation accelerationInformation);

    @Update
    public void update(AccelerationInformation accelerationInformation);

    @Delete
    public void delete(AccelerationInformation accelerationInformation);

    @Query("SELECT * FROM measurement_DB WHERE id = :id")
    public AccelerationInformation findById(String id);

    @Query("SELECT * FROM measurement_DB")
    public List<AccelerationInformation> getItems();

    @Query("SELECT * FROM measurement_DB")
    public LiveData<List<AccelerationInformation>> getItemsAsLiveData();

    @Query("SELECT * FROM measurement_DB WHERE timestamp = :timestamp")
    public AccelerationInformation findByTimestamp(long timestamp);

    @Query("SELECT * FROM measurement_DB WHERE timestamp >= :start AND timestamp <= :stop")
    public List<AccelerationInformation> findByTimerange(long start, long stop);

}


