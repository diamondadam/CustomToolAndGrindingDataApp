package com.customtoolandgrinding.customtooldataapp.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.customtoolandgrinding.customtooldataapp.models.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class TransactionDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static volatile TransactionDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TransactionDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TransactionDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TransactionDatabase.class, "transaction_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
