package com.example.customtooldataapp.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.customtooldataapp.models.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO create migration strategy
@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class TransactionRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static volatile TransactionRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TransactionRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TransactionRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TransactionRoomDatabase.class, "transaction_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
