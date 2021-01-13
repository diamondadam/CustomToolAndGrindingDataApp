package com.customtoolandgrinding.customtooldataapp.source;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    private Context context;
    private final String employeeId;
    private final TransactionDao transactionDao;
    private final PunchHoleDao punchHoleDao;

    private LiveData<List<Transaction>> currentTransactions;
    private LiveData<List<Transaction>> pastTransactions;
    private LiveData<List<PunchHole>> times;
    private MutableLiveData<HashMap<Integer, Long>> thisWeeksHours = new MutableLiveData<>();

    public static TransactionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TransactionRepository(application);
        }
        return instance;
    }

    public TransactionRepository(Application application) {
        this.context = context;
        SharedPreferences sharedPreferences = application.getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeId = sharedPreferences.getString("ID", null);

        TransactionDatabase tDb = TransactionDatabase.getDatabase(application);
        PunchHoleDatabase cDb = PunchHoleDatabase.getDatabase(application);

        transactionDao = tDb.transactionDao();
        punchHoleDao = cDb.clockDao();


        updatePunchHoles();
        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            try {
                syncDatabases();
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


    public List<PunchHole> getPunchCardByDay(String day) {
        return punchHoleDao.selectByDay(day);
    }

    public LiveData<List<PunchHole>> getPunchHoles() {
        return times;
    }

    public void updateTransactions() {
        TransactionDatabase.databaseWriteExecutor.execute(() -> {
            try {
                syncDatabases();
            } catch (ConnectionError connectionError) {
                transactionDao.insertList(setErrorData(connectionError));
                currentTransactions = transactionDao.loadTransactions("No");
                pastTransactions = transactionDao.loadTransactions("Yes");
                Log.d("syncDatabases()", "Error getTransactions()...");
                Log.d("syncDatabases()", connectionError.getMessage());
                return;
            }
            currentTransactions = transactionDao.loadTransactions("No");
            pastTransactions = transactionDao.loadTransactions("Yes");
        });
    }

    public LiveData<List<Transaction>> getActiveTransactions() {
        Log.d("getActiveTransactions()", "Checking transactions...");
        return currentTransactions;
    }

    public LiveData<List<Transaction>> getInactiveTransactions() {
        Log.d("getInactiveTransactions", "Checking transactions...");
        return pastTransactions;
    }

    public LiveData<HashMap<Integer, Long>> getThisWeeksHours(){
        return thisWeeksHours;
    }

    public boolean isPunchedIn(){
        try {
            return new JobBossClient(employeeId).isPunchedIn();
        } catch (ConnectionError connectionError) {
            connectionError.printStackTrace();
        }
        Log.d("isPunchedIn()", "Returning false...");
        return false;
    }

    private void syncDatabases() throws ConnectionError {
        List<Transaction> remoteTransactions = new JobBossClient(employeeId).getTransactions();
        Log.d("syncDatabases()", "Executing getTransactions()...");

        List<Transaction> localTransactions = transactionDao.selectAll();
        updatePunchHoles();
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
                TransactionDatabase.databaseWriteExecutor.execute(() -> {
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

    private void updatePunchHoles() {
        JobBossClient jobBossClient = new JobBossClient(employeeId);
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
        PunchHoleDatabase.databaseWriteExecutor.execute(() ->{
            try {
                punchHoleDao.insert(jobBossClient.getClockInOutTime());
                times = punchHoleDao.selectAll(today);
            } catch (ConnectionError connectionError) {
                connectionError.printStackTrace();
            }
        });
        processWeeklyHours();
        times = punchHoleDao.selectAll(today);
    }

    private void processWeeklyHours() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        HashMap<Integer, Long> weeklyHoursHashMap = initializeHashMap();
        Calendar cal = Calendar.getInstance();
        PunchHoleDatabase.databaseWriteExecutor.execute(() -> {
            while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                try{
                    processPunchCard(weeklyHoursHashMap, getPunchCardByDay(sdf.format(cal.getTime())), cal.get(Calendar.DAY_OF_WEEK));
                }catch(ParseException e){
                    Log.d("Parse Exception", e.getMessage());
                }
                //Go to the previous day
                cal.add(Calendar.DATE, -1);
            }
            thisWeeksHours.postValue(weeklyHoursHashMap);
        });
        thisWeeksHours.postValue(weeklyHoursHashMap);
    }

    private void processPunchCard(HashMap<Integer, Long> weeklyHoursHashMap, List<PunchHole> punchCards, Integer dayOfTheWeek) throws ParseException {
        PunchHole previousPunchHole = null;

        for(PunchHole punchHole: punchCards){
            if(punchHole.getDate().contains("Clock In Time: ")){
                previousPunchHole = punchHole;
            } else if(previousPunchHole != null){
                addData(weeklyHoursHashMap, previousPunchHole, punchHole, dayOfTheWeek);
                //Reset Previous
                previousPunchHole = null;
            }
        }
    }

    private void addData(HashMap<Integer, Long> weeklyHoursHashMap, PunchHole in, PunchHole out, Integer dayOfTheWeek) throws ParseException {
        SimpleDateFormat punchHoleFormat = new SimpleDateFormat("MMM d yyyy HH:mm a", Locale.US);
        Long totalTimeForTheDay = weeklyHoursHashMap.get(dayOfTheWeek) + punchHoleFormat.parse(out.getDate()).getTime() - punchHoleFormat.parse(in.getDate()).getTime();
        weeklyHoursHashMap.put(dayOfTheWeek, totalTimeForTheDay);

    }

    private HashMap<Integer, Long> initializeHashMap(){
        HashMap<Integer, Long> map = new HashMap<>();
        map.put(Calendar.SUNDAY, 0L);
        map.put(Calendar.MONDAY, 0L);
        map.put(Calendar.TUESDAY, 0L);
        map.put(Calendar.WEDNESDAY, 0L);
        map.put(Calendar.THURSDAY, 0L);
        map.put(Calendar.FRIDAY, 0L);
        map.put(Calendar.SATURDAY, 0L);
        return map;
    }



}
