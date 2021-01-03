package com.example.customtooldataapp.ui.hours;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class YourHoursViewModel extends ViewModel {
    private final LiveData<List<Transaction>> transactions;

    public YourHoursViewModel(Application application) {
        Log.d("YourHoursViewModel", "Constructor");
        TransactionRepository transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getInactiveTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("YourHoursViewModel", "getTransactions()");
        return transactions;
    }
}
