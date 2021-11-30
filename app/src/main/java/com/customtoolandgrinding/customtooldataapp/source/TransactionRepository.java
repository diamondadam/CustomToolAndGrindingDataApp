package com.customtoolandgrinding.customtooldataapp.source;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.navigation.NavDeepLinkBuilder;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.alarums.Alarm;
import com.customtoolandgrinding.customtooldataapp.models.PunchHole;
import com.customtoolandgrinding.customtooldataapp.models.Job;
import com.customtoolandgrinding.customtooldataapp.models.Operation;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.source.local.PunchHoleDatabase;
import com.customtoolandgrinding.customtooldataapp.source.local.PunchHoleDao;
import com.customtoolandgrinding.customtooldataapp.source.local.TransactionDao;
import com.customtoolandgrinding.customtooldataapp.source.local.TransactionDatabase;
import com.customtoolandgrinding.customtooldataapp.source.remote.JobBossClient;
import com.customtoolandgrinding.customtooldataapp.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class TransactionRepository {
    private static TransactionRepository instance;

    private final String employeeID;
    private final TransactionDao transactionDao;
    private final PunchHoleDao punchHoleDao;
    private final String today = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;
    private LiveData<List<PunchHole>> times;
    private HashMap<String, Transaction> currentTransactionList;
    private HashMap<String, Transaction> map;

    public static TransactionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application) {
        //Obtain employee id
        SharedPreferences sharedPreferences = application.getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeID = sharedPreferences.getString("ID", null);
        map = new HashMap();

        //Obtain databases
        TransactionDatabase tDb = TransactionDatabase.getDatabase(application);
        PunchHoleDatabase cDb = PunchHoleDatabase.getDatabase(application);

        //Initialize DAOs
        transactionDao = tDb.transactionDao();
        punchHoleDao = cDb.clockDao();

        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
        times = punchHoleDao.selectAll(today);
    }

    public void syncDatabasesThreaded() {
        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            updatePunchHoles();
            //Current transactions list
            List<Transaction> transactions = transactionDao.selectAll();

            //Initialize HashMap to current values
            for(Transaction transaction : transactions){
                map.put(transaction.getTransactionPath(), transaction);
            }

            //Map not is only remote
            map = new JobBossClient(employeeID).getTransactions(map);

            if (map.isEmpty()) {
                transactionDao.deleteAllJobs();
                return;
            }else{
                //For each remote transaction
                for (Transaction transaction : map.values()) {
                    transactions.remove(transaction);
                    transactionDao.insert(transaction);
                }
            }

            //Remove old transactions
            for(Transaction transaction : transactions){
                deleteTransaction(transaction);
            }

            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });
    }

    public void syncDatabases() {
        updatePunchHoles();
        //Current transactions list
        List<Transaction> transactions = transactionDao.selectAll();

        //Initialize HashMap to current values
        for(Transaction transaction : transactions){
            map.put(transaction.getTransactionPath(), transaction);
        }

        //Map not is only remote
        map = new JobBossClient(employeeID).getTransactions(map);

        if (map.isEmpty()) {
            transactionDao.deleteAllJobs();
            return;
        }else{
            //For each remote transaction
            for (Transaction transaction : map.values()) {
                transactions.remove(transaction);
                transactionDao.insert(transaction);
            }
        }

        //Remove old transactions
        for(Transaction transaction : transactions){
            deleteTransaction(transaction);
        }

        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
    }

    private void updatePunchHoles() {
        JobBossClient jobBossClient = new JobBossClient(employeeID);
        punchHoleDao.insert(jobBossClient.getClockInOutTime());
        times = punchHoleDao.selectAll(today);
    }

    public boolean isPunchedIn() {
        return new JobBossClient(employeeID).isPunchedIn();
    }

    /*
     * Transaction DAO functions
     * */
    public void deleteTransaction(Transaction transaction) {
            transactionDao.deleteTransaction(transaction.getTranID());
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
    }

    /*
     * Public getters
     * */
    public LiveData<List<Transaction>> getActiveTransactions() {
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getInactiveTransactions() {
        return pastTransactions;
    }

    public LiveData<List<PunchHole>> getPunchHoles() {
        return times;
    }

    public List<PunchHole> getPunchCardByDay(String day) {
        return punchHoleDao.selectByDay(day);
    }


}
