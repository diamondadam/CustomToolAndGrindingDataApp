package com.example.customtooldataapp.source;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customtooldataapp.model.Employee;
import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.remote.JobBossClient;

import java.util.ArrayList;
import java.util.List;

/*  String transactionLink = "";
    String transactionLink1 = "";
    String transactionLink2 = "";
    emp.addTransaction(transactionLink, "99999");
    emp.addTransaction(transactionLink1, "88888");
    emp.addTransaction(transactionLink2, "88888");
    */

public class TransactionsRepository {
    private static TransactionsRepository instance;
    private JobBossClient jobBossClient;
    private MutableLiveData<List<Transaction>> transactions;
    private Employee employee;

    public static TransactionsRepository getInstance() {
        if (instance == null) {
            Log.d("TransactionsRepository", "Singleton Create");
            instance = new TransactionsRepository();
        }
        Log.d("TransactionsRepository", "Singleton Get");
        return instance;
    }

    private TransactionsRepository() {
        Log.d("TransactionsRepository", "Initializing JBC");

        transactions = new MutableLiveData<>();
        Log.d("TransactionsRepository", "Requesting Transactions...");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    jobBossClient = JobBossClient.getInstance();
                    transactions.postValue(jobBossClient.getTransactions());
                } catch (Exception e) {
                    Log.d("TransactionsRepository", "Exception...");
                    Log.d("TransactionsRepository", e.getMessage());
                }
            }
        };
        thread.start();
        if(transactions.getValue() == null){
            Log.d("TransactionsRepository", "Transactions are null...");
        }else{
            Log.d("TransactionsRepository", "Size: " + transactions.getValue().size());
        }
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("TransactionsRepository", "getTransactions()");
        return transactions;
    }

}
