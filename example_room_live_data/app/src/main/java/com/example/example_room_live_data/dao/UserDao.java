package com.example.example_room_live_data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.example_room_live_data.data.User;

@Dao
public abstract class UserDao {

    // USER
    @Query("SELECT * FROM user WHERE email = :email")
    public abstract LiveData<User> getUserByEmail (String email);

    // ACCDATA
    @Query("SELECT * FROM user")
    // <ArrayList<User>>
    public abstract LiveData<User> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(User user);
}
