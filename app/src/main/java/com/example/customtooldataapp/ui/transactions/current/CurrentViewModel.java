package com.example.customtooldataapp.ui.transactions.current;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.data.model.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class CurrentViewModel extends ViewModel {

    private TransactionRepository transactionsRepository;
    private LiveData<List<Transaction>> transactions;

    public CurrentViewModel(Application application) {
        Log.d("CurrentViewModel", "Constructor");
        transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getCurrentTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("CurrentViewModel", "getTransactions()");
        return transactions;
    }
}

