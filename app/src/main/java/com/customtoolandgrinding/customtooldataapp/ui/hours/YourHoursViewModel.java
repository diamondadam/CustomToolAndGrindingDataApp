package com.customtoolandgrinding.customtooldataapp.ui.hours;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;

import java.util.HashMap;

public class YourHoursViewModel extends ViewModel {
    private final Application application;
    private LiveData<HashMap<Integer, Long>> weeklyHours;

    public YourHoursViewModel(Application application) {
        this.application = application;
        TransactionRepository transactionRepository = TransactionRepository.getInstance(application);
        weeklyHours = transactionRepository.getThisWeeksHours();
    }

    public LiveData<HashMap<Integer, Long>> getWeeklyHours(){
        return this.weeklyHours;
    }
}
