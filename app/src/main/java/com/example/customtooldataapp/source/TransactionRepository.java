package com.example.customtooldataapp.source;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customtooldataapp.models.Job;
import com.example.customtooldataapp.models.Operation;
import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.local.TransactionDao;
import com.example.customtooldataapp.source.local.TransactionRoomDatabase;
import com.example.customtooldataapp.source.remote.JobBossClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TransactionRepository {


    private static TransactionRepository instance;

    private final String employeeId;
    private final TransactionDao transactionDao;

    private MutableLiveData<List<Transaction>> errorMutableLiveData;

    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;

    public static TransactionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("ID", null);

        TransactionRoomDatabase db = TransactionRoomDatabase.getDatabase(application);
        transactionDao = db.transactionDao();

        JobBossClient jobBossClient = new JobBossClient(employeeId);
        Log.d("TransactionRepository()", "Starting databaseWriteExecutor...");

        TransactionRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d("databaseWriteExecutor()", "Deleting all jobs...");
            transactionDao.deleteAllJobs();
            Log.d("databaseWriteExecutor()", "Retrieving remote transactions...");
            try {
                transactionDao.insertList(jobBossClient.getTransactions());
                Log.d("databaseWriteExecutor()", "Remote transactions retrieved...");
            } catch (ConnectionError connectionError) {
                Log.d("databaseWriteExecutor()", "ConnectionError in getTransactions()...");
                Log.d("databaseWriteExecutor()", connectionError.getMessage());

                transactionDao.insertList(setErrorData(connectionError));
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
                return;
            }
            Log.d("databaseWriteExecutor()", "Loading transactions...");
            Log.d("databaseWriteExecutor()", "Room size..." + transactionDao.selectAll().size());
            for(Transaction transaction: transactionDao.selectAll()){
                Log.d("databaseWriteExecutor()", transaction.toString());
            }
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");

            Log.d("databaseWriteExecutor()", "Transactions loaded...");
        });

        Log.d("TransactionRepository()", "Loading transactions...");
        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
        Log.d("TransactionRepository()", "Transactions loaded...");

    }

    public void syncDatabases() {
        Log.d("syncDatabases()", "Executing getTransactions()...");
        List<Transaction> remoteTransactions = null;
        try {
            remoteTransactions = new JobBossClient(employeeId).getTransactions();
        } catch (ConnectionError connectionError) {
            Log.d("syncDatabases()", "Error getTransactions()...");
            Log.d("syncDatabases()", connectionError.getMessage());
            transactionDao.insertList(setErrorData(connectionError));
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");

            return;
        }
        List<Transaction> localTransactions = transactionDao.selectAll();
        Log.d("syncDatabases()", "Filtering database...");

        if (remoteTransactions.isEmpty()) {
            transactionDao.deleteAllJobs();
            Log.d("syncDatabases()", "No remote transactions deleting all and returning...");
            return;
        }

        /*
         * For each transaction in local database, check if it exists in the remote database.
         * If not remove it from the local database.
         */
        for (Transaction local : localTransactions) {
            if (!remoteTransactions.contains(local)) {
                transactionDao.deleteOne(local.getTranID());
                Log.d("syncDatabases()", "Deleting transaction...");
            }
        }

        /*
         * For each transaction in remote database, check if it exists in the local database.
         * If not add it to the local database.
         */
        for (Transaction transaction : remoteTransactions) {
            if (!localTransactions.contains(transaction)) {
                TransactionRoomDatabase.databaseWriteExecutor.execute(() -> {
                    Log.d("syncDatabases()", "Inserting transaction...");
                    transactionDao.insert(transaction);
                });
            }
        }

        Log.d("syncDatabases()", "Loading transactions...");
        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
        Log.d("syncDatabases()", "Transactions loaded...");
    }

    public LiveData<List<Transaction>> getActiveTransactions() {
        Log.d("getActiveTransactions()", "Checking transactions...");
        if (currentTransactions.getValue() != null) {
            Log.d("getActiveTransactions()", "Transactions not null...");
            for (Transaction transaction : currentTransactions.getValue()) {
                //Log.d("currentTransactions", transaction.toString());
            }
        }
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getInactiveTransactions() {

        Log.d("getInactiveTransactions", "Checking transactions...");
        if (pastTransactions.getValue() != null) {
            Log.d("getInactiveTransactions", "Transactions not null...");
            for (Transaction transaction : pastTransactions.getValue()) {
                //Log.d("pastTransactions", transaction.toString());
            }
        }
        return pastTransactions;
    }

    private ArrayList<Transaction> setErrorData(ConnectionError connectionError){

        Transaction activeErrorTransaction = new Transaction(connectionError);
        activeErrorTransaction.setTranID("Active Error");
        activeErrorTransaction.setLogout("No");
        Job job = new Job("");
        Operation operation = new Operation("");
        activeErrorTransaction.setJob(job);
        activeErrorTransaction.setOperation(operation);

        Transaction inactiveErrorTransaction = new Transaction(connectionError);
        inactiveErrorTransaction.setTranID("Inactive Error");
        inactiveErrorTransaction.setLogout("Yes");
        job = new Job("");
        operation = new Operation("");
        inactiveErrorTransaction.setJob(job);
        inactiveErrorTransaction.setOperation(operation);

        Log.d("setErrorData()", activeErrorTransaction.toString());
        Log.d("setErrorData()", inactiveErrorTransaction.toString());
        return new ArrayList<>(Arrays.asList(activeErrorTransaction, inactiveErrorTransaction));
    }
}
