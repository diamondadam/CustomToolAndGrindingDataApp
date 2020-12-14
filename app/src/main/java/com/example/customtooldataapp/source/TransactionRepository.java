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
import java.util.function.LongFunction;

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

    public static TransactionRepository getInstance(Application application){
        if(instance == null){
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application){
        TransactionRoomDatabase db = TransactionRoomDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        jobBossClient = new JobBossClient("0163");
        Log.d("TransactionRepository", "Populating transactions...");
        TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
            Log.d("DatabaseExecutor", "Populating transactions...");
            transactionDao.insertList(jobBossClient.populateTransactions());
            Log.d("DatabaseExecutor", "Complete...");
            Log.d("DatabaseExecutor", "Size..." + transactionDao.selectAll().size());
            for (Transaction transaction: transactionDao.selectAll()){
                Log.d("DatabaseExecutor",  transaction.toString());
            }
        });
        Log.d("TransactionRepository", "Loading transactions...");
        currentTransactions = transactionDao.loadTransactions("Yes");
        pastTransactions = transactionDao.loadTransactions("No");
    }

    public  void syncDatabases(){
        Log.d("syncDatabases()", "Starting Executor...");
        TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
            List<Transaction> transactions = jobBossClient.populateNewTransactions();
            List<Transaction> databaseTransactions = transactionDao.selectAll();

            for (Transaction transaction: databaseTransactions){
                if(!transactions.contains(transaction)){
                    //If the transaction is no longer in the remote database remove it from local
                    transactionDao.deleteOne(transaction.getTranID());
                }
            }

            for (Transaction transaction: transactions){
                if(!databaseTransactions.contains(transaction)){
                    //If transaction is in remote database but not local insert it.
                    TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
                        transactionDao.insert(transaction);
                    });
                }
            }
        });
        currentTransactions = transactionDao.loadTransactions("Yes");
        pastTransactions = transactionDao.loadTransactions("No");
    }

    public void insert(Transaction transaction){
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
}
