package com.example.customtooldataapp.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.customtooldataapp.models.ClockInAndOut;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ClockInAndOut.class}, version = 1, exportSchema = false)
public abstract class ClockRoomDatabase extends RoomDatabase {

    public abstract ClockDao clockDao();

    private static volatile ClockRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ClockRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ClockRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ClockRoomDatabase.class, "clock_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
