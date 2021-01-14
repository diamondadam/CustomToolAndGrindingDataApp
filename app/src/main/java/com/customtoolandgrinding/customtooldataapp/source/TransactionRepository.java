package com.customtoolandgrinding.customtooldataapp.source;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.customtoolandgrinding.customtooldataapp.R;
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

import static android.content.Context.MODE_PRIVATE;

public class TransactionRepository {
    private static TransactionRepository instance;

    private final String employeeId;
    private final TransactionDao transactionDao;
    private final PunchHoleDao punchHoleDao;
    private final String today = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;
    private LiveData<List<PunchHole>> times;

    public static TransactionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application) {
        //Obtain employee id
        SharedPreferences sharedPreferences = application.getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("ID", null);

        //Obtain databases
        TransactionDatabase tDb = TransactionDatabase.getDatabase(application);
        PunchHoleDatabase cDb = PunchHoleDatabase.getDatabase(application);

        //Initialize DAOs
        transactionDao = tDb.transactionDao();
        punchHoleDao = cDb.clockDao();

        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            try {
                //Update the punch holes
                updatePunchHoles();
                //Sync the databases
                syncDatabases();
            } catch (ConnectionError connectionError) {
                transactionDao.insertList(setErrorData(connectionError));
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
            }
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });
        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
        times = punchHoleDao.selectAll(today);
    }

    /*
     * Public function to call syncDatabases()
     * */
    public void updateTransactions() {
        //Sync databases
        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            try {
                syncDatabases();
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
                updatePunchHoles();
            } catch (ConnectionError connectionError) {
                transactionDao.insertList(setErrorData(connectionError));
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
                Log.d("syncDatabases()", "Error getTransactions()...");
                Log.d("syncDatabases()", connectionError.getMessage());
                return;
            }

        });
    }

    /*
     * Public function only called when the sync button is pressed.
     * Main activity handles threading.
     * */
    public void syncButtonPressed() {
        try {
            syncDatabases();
        } catch (ConnectionError connectionError) {
            transactionDao.insertList(setErrorData(connectionError));
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
            Log.d("syncDatabases()", "Error getTransactions()...");
            Log.d("syncDatabases()", connectionError.getMessage());
        }
    }

    public boolean isPunchedIn() {
        try {
            return new JobBossClient(employeeId).isPunchedIn();
        } catch (ConnectionError connectionError) {
            transactionDao.insertList(setErrorData(connectionError));
        }
        return false;
    }

    private void syncDatabases() throws ConnectionError {

        List<Transaction> remoteTransactions = new JobBossClient(employeeId).getTransactions();
        Log.d("syncDatabases()", "Executing getTransactions()...");

        List<Transaction> localTransactions = transactionDao.selectAll();
        updatePunchHoles();
        Log.d("syncDatabases()", "Filtering database...");
        if (remoteTransactions.isEmpty()) {
            Log.d("syncDatabases()", "No remote transactions deleting all and returning...");
            transactionDao.deleteAllJobs();
            return;
        }

        /*
         * For each transaction in local database, check if it exists in the remote database.
         * If not remove it from the local database.
         */
        for (Transaction local : localTransactions) {
            if (!remoteTransactions.contains(local)) {
                Log.d("syncDatabases()", "Deleting transaction...");
                transactionDao.deleteOne(local.getTranID());
            }
        }

        /*
         * For each transaction in remote database, check if it exists in the local database.
         * If not add it to the local database.
         */
        for (Transaction transaction : remoteTransactions) {
            if (!localTransactions.contains(transaction)) {
                Log.d("syncDatabases()", "Inserting transaction...");
                transactionDao.insert(transaction);
            }
        }

        Log.d("syncDatabases()", "Loading transactions...");
        currentTransactions = transactionDao.loadTransactions("No");
        pastTransactions = transactionDao.loadTransactions("Yes");
        Log.d("syncDatabases()", "Transactions loaded...");

    }

    private void updatePunchHoles() {
        JobBossClient jobBossClient = new JobBossClient(employeeId);

        try {
            punchHoleDao.insert(jobBossClient.getClockInOutTime());
            times = punchHoleDao.selectAll(today);
        } catch (ConnectionError connectionError) {
            transactionDao.insertList(setErrorData(connectionError));
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
            times = punchHoleDao.selectAll(today);
        }
    }

    /*
     * Transaction DAO functions
     * */
    public void deleteTransactionByPath(String path) {
        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.deleteTransactionByPath(path);
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });
    }

    /*
     * Public getters
     * */
    public LiveData<List<Transaction>> getActiveTransactions() {
        Log.d("getActiveTransactions()", "Checking transactions...");
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getInactiveTransactions() {
        Log.d("getInactiveTransactions", "Checking transactions...");
        return pastTransactions;
    }

    public LiveData<List<PunchHole>> getPunchHoles() {
        return times;
    }

    public List<PunchHole> getPunchCardByDay(String day) {
        return punchHoleDao.selectByDay(day);
    }

    /*
     * Miscellaneous Functions
     * */
    private ArrayList<Transaction> setErrorData(ConnectionError connectionError) {
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
