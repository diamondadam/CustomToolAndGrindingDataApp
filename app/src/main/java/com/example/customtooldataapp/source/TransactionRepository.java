package com.example.customtooldataapp.source;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.local.TransactionDao;
import com.example.customtooldataapp.source.local.TransactionRoomDatabase;
import com.example.customtooldataapp.source.remote.JobBossClient;

import java.util.List;

public class TransactionRepository {
    private static TransactionRepository instance;

    private final JobBossClient jobBossClient;
    private final TransactionDao transactionDao;

    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;

    public static TransactionRepository getInstance(Application application){
        if(instance == null){
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application){
        TransactionRoomDatabase db = TransactionRoomDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        //TODO: Pass or get employee id
        jobBossClient = new JobBossClient("0163");

        Log.d("TransactionRepository", "Beginning Executor...");

        TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
            //transactionDao.deleteAllJobs();
            Log.d("DatabaseExecutor", "Getting remote transactions...");
            transactionDao.insertList(jobBossClient.getTransactions());
            Log.d("DatabaseExecutor", "Complete...");
            Log.d("DatabaseExecutor", "Size..." + transactionDao.selectAll().size());

            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });

        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");

    }

    public  void syncDatabases(){

        TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
            List<Transaction> remoteTransactions = jobBossClient.getTransactions();
            List<Transaction> localTransactions = transactionDao.selectAll();

            /*
            * For each transaction in local database, check if it exists in the remote database.
            * If not remove it from the local database.
            */
            for (Transaction local: localTransactions){
                if(!remoteTransactions.contains(local)){
                    transactionDao.deleteOne(local.getTranID());
                }
            }
            /*
             * For each transaction in remote database, check if it exists in the local database.
             * If not add it to the local database.
             */
            for (Transaction transaction: remoteTransactions){
                if(!localTransactions.contains(transaction)){
                    TransactionRoomDatabase.databaseWriteExecutor.execute(()->{
                        transactionDao.insert(transaction);
                    });
                }
            }
        });

        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
    }

    public LiveData<List<Transaction>> getCurrentTransactions() {
        Log.d("TransactionsRepository", "getCurrentTransactions()");
        if(currentTransactions.getValue() != null){
            for (Transaction transaction: currentTransactions.getValue()){
                Log.d("currentTransactions", transaction.toString());
            }
        }
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getPastTransactions(){

        Log.d("TransactionsRepository", "getPastTransactions()");
        if(pastTransactions.getValue() != null){
            for (Transaction transaction: pastTransactions.getValue()){
                Log.d("pastTransactions", transaction.toString());
            }
        }
        return pastTransactions;
    }
}