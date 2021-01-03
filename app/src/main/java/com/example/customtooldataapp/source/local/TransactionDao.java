package com.example.customtooldataapp.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.customtooldataapp.models.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaction transaction);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Transaction> transactions);

    @Query("DELETE FROM transaction_table")
    void deleteAllJobs();

    @Query("SELECT * FROM transaction_table WHERE logout = :logout_status")
    LiveData<List<Transaction>> loadTransactions(String logout_status);

    @Query("SELECT * FROM transaction_table WHERE logout = :logout_status")
    List<Transaction> getNonLiveTransactions(String logout_status);

    @Query("SELECT * FROM transaction_table WHERE operationNumber = :operationNumber")
    LiveData<List<Transaction>> findByOperationNumber(String operationNumber);


    @Query("SELECT * FROM transaction_table")
    List<Transaction> selectAll();

    @Query("DELETE FROM transaction_table WHERE tranID = :transactionId")
    void deleteOne(String transactionId);
}
