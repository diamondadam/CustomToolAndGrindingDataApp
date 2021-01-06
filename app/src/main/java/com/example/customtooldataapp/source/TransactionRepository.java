package com.example.customtooldataapp.source;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.customtooldataapp.models.ClockInAndOut;
import com.example.customtooldataapp.models.Job;
import com.example.customtooldataapp.models.Operation;
import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.local.ClockDao;
import com.example.customtooldataapp.source.local.ClockRoomDatabase;
import com.example.customtooldataapp.source.local.TransactionDao;
import com.example.customtooldataapp.source.local.TransactionRoomDatabase;
import com.example.customtooldataapp.source.remote.JobBossClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class TransactionRepository {
    private static TransactionRepository instance;

    private final String employeeId;
    private final TransactionDao transactionDao;
    private final ClockDao clockDao;

    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;
    private LiveData<List<ClockInAndOut>> times;


    public static TransactionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("ID", null);

        TransactionRoomDatabase tDb = TransactionRoomDatabase.getDatabase(application);
        ClockRoomDatabase cDb = ClockRoomDatabase.getDatabase(application);

        transactionDao = tDb.transactionDao();
        clockDao = cDb.clockDao();

        JobBossClient jobBossClient = new JobBossClient(employeeId);

        TransactionRoomDatabase.databaseWriteExecutor.execute(() -> {
            try {
                syncDatabases(jobBossClient.getTransactions());
                updateClock();
            } catch (ConnectionError connectionError) {
                transactionDao.insertList(setErrorData(connectionError));
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
                return;
            }

            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });
        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
    }

    public void updateClock() throws ConnectionError {
        JobBossClient jobBossClient = new JobBossClient(employeeId);
        clockDao.insert(jobBossClient.getClockInOutTime());
        setTimes();
    }

    public LiveData<List<ClockInAndOut>> setTimes(){
        String str = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
        return clockDao.selectAll(str);
    }

    public LiveData<List<ClockInAndOut>> getTimes() {
        if(times == null){
           times = instance.setTimes();
        }
        return times;
    }

    public void updateTransactions() {
        try {
            List<Transaction> remoteTransactions = new JobBossClient(employeeId).getTransactions();
            updateClock();
            syncDatabases(remoteTransactions);
        } catch (ConnectionError connectionError) {
            Log.d("syncDatabases()", "Error getTransactions()...");
            Log.d("syncDatabases()", connectionError.getMessage());
            transactionDao.insertList(setErrorData(connectionError));
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        }
    }

    public void syncDatabases(List<Transaction> remoteTransactions) {
        Log.d("syncDatabases()", "Executing getTransactions()...");

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
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getInactiveTransactions() {
        Log.d("getInactiveTransactions", "Checking transactions...");
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
