package com.customtoolandgrinding.customtooldataapp.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.customtoolandgrinding.customtooldataapp.models.PunchHole;

import java.util.List;

@Dao
public interface PunchHoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PunchHole punchHole);

    @Query("SELECT * FROM punch_hole_table WHERE day = :day")
    LiveData<List<PunchHole>> selectAll(String day);

    @Query("SELECT * FROM punch_hole_table WHERE day = :day")
    List<PunchHole> selectByDay(String day);

    @Query("SELECT * FROM punch_hole_table WHERE day = :day")
    PunchHole selectOne(String day);
}
