package com.example.customtooldataapp.ui.hours;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.models.ClockInAndOut;
import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YourHoursViewModel extends ViewModel {
    private final LiveData<List<Transaction>> transactions;
    private Application application;

    public YourHoursViewModel(Application application) {
        Log.d("YourHoursViewModel", "Constructor");
        this.application = application;
        TransactionRepository transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getInactiveTransactions();
    }

    public LiveData<List<Transaction>> getPunchCards() {
        Log.d("YourHoursViewModel", "getPunchCards()");
        TransactionRepository transactionRepository = TransactionRepository.getInstance(application);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat clockTime = new SimpleDateFormat("MMM d yyyy HH:mm a", Locale.US);
        Calendar cal = Calendar.getInstance();

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            List<ClockInAndOut> punchCard = transactionRepository.getPunchCard(sdf.format(cal.getTime()));
            for(ClockInAndOut clockInAndOut: punchCard){
                Date punchIn = null;
                Date punchOut = null;
                //Clock Out Time: Jan 6 2021 6:53 PM
                if(clockInAndOut.getDate().contains("Clock Out")){
                    //Process Clock Out
                    String pureDateString = clockInAndOut.getDate().replace("Clock Out Time: ", "");
                    try {
                        punchOut = clockTime.parse(pureDateString);
                    } catch (ParseException e) {
                        Log.d("Parse Error", e.getMessage());
                        e.printStackTrace();
                    }
                    Log.d("Pure Data String", pureDateString);
                    Log.d("Date", punchOut.toString());
                }else{
                    //Process Clock In
                    String pureDateString = clockInAndOut.getDate().replace("Clock Out Time: ", "");
                    try {
                        punchIn = clockTime.parse(pureDateString);
                    } catch (ParseException e) {
                        Log.d("Parse Error", e.getMessage());
                        e.printStackTrace();
                    }
                    Log.d("Pure Data String", pureDateString);
                    Log.d("Date", punchIn.toString());
                }
            }
            cal.add(Calendar.DATE, -1);
        }
        return transactions;
    }
}
