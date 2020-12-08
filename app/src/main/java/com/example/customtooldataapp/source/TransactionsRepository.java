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
    private final Employee employee;
    private JobBossClient jobBossClient;
    private MutableLiveData<List<Transaction>> currentTransactions;
    private MutableLiveData<List<Transaction>> pastTransactions;


    public static TransactionsRepository getInstance() {
        if (instance == null) {
            Log.d("TransactionsRepository", "Singleton Create");
            instance = new TransactionsRepository();
        }
        return instance;
    }

    private TransactionsRepository() {
        employee = Employee.getInstance();
        Log.d("TransactionsRepository", "Initializing JBC");

        currentTransactions = new MutableLiveData<>();
        pastTransactions = new MutableLiveData<>();

        Log.d("TransactionsRepository", "Requesting Transactions...");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    jobBossClient = new JobBossClient();
                    jobBossClient.init(employee);
                    List<Transaction> currentTemp = new ArrayList<>();
                    List<Transaction> pastTemp = new ArrayList<>();
                    for(Transaction transaction: jobBossClient.getTransactions(employee)){
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

    public LiveData<List<Transaction>> getCurrentTransactions() {
        Log.d("TransactionsRepository", "getCurrentTransactions()");
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getPastTransactions(){
        Log.d("TransactionsRepository", "getPastTransactions()");
        return pastTransactions;
    }

}
