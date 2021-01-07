package com.example.customtooldataapp.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.customtooldataapp.models.ClockInAndOut;

import java.util.List;

@Dao
public interface ClockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClockInAndOut clockInAndOut);

    @Query("SELECT * FROM clock_in_and_out_table WHERE day = :day")
    LiveData<List<ClockInAndOut>> selectAll(String day);

    @Query("SELECT * FROM clock_in_and_out_table WHERE day = :day")
    List<ClockInAndOut> selectByDay(String day);

    @Query("SELECT * FROM clock_in_and_out_table WHERE day = :day")
    ClockInAndOut selectOne(String day);
}
