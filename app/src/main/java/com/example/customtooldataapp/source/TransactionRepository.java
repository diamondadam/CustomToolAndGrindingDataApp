package com.example.customtooldataapp.source;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customtooldataapp.model.Employee;
import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.local.TransactionDao;
import com.example.customtooldataapp.source.local.TransactionRoomDatabase;
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

public class TransactionRepository {
    private static TransactionRepository instance;
    private JobBossClient jobBossClient;
    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;

    private TransactionDao transactionDao;
    
    public TransactionRepository(Application application){
        TransactionRoomDatabase db = TransactionRoomDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        currentTransactions = transactionDao.loadTransactions("Yes");
        pastTransactions = transactionDao.loadTransactions("No");
    }

    void insert(Transaction transaction){
        TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
            transactionDao.insert(transaction);
        });
    }

    public LiveData<List<Transaction>> getCurrentTransactions() {
        Log.d("TransactionsRepository", "getCurrentTransactions()");
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getPastTransactions(){
        Log.d("TransactionsRepository", "getPastTransactions()");
        return pastTransactions;
    }


/*
    private TransactionRepository() {
        Log.d("TransactionsRepository", "Initializing JBC");

        currentTransactions = new MutableLiveData<>();
        pastTransactions = new MutableLiveData<>();

        Log.d("TransactionsRepository", "Requesting Transactions...");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    jobBossClient = new JobBossClient();
                    jobBossClient.init();
                    List<Transaction> currentTemp = new ArrayList<>();
                    List<Transaction> pastTemp = new ArrayList<>();
                    for(Transaction transaction: jobBossClient.getTransactions()){
                        if(transaction.getLogout().equals("No")){
                            currentTemp.add(transaction);
                        }else{
                            pastTemp.add(transaction);
                        }
                    }
                    currentTransactions.postValue(currentTemp);
                    pastTransactions.postValue(pastTemp);
                } catch (Exception e) {
                    Log.d("TransactionsRepository", "Exception...");
                }
            }
        };
        thread.start();
    }
*/



}
