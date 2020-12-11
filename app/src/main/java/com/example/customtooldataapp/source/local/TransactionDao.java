package com.example.customtooldataapp.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.customtooldataapp.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Transaction transaction);

    @Query("DELETE FROM transaction_table")
    void deleteAllJobs();

    @Query("SELECT * FROM transaction_table WHERE logout = :logout_status")
    LiveData<List<Transaction>> loadTransactions(String logout_status);

}
