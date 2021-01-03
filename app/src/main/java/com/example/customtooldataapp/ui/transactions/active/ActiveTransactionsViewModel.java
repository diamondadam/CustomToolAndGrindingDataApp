package com.example.customtooldataapp.ui.transactions.active;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class ActiveTransactionsViewModel extends ViewModel {

    private final LiveData<List<Transaction>> transactions;

    public ActiveTransactionsViewModel(Application application) {
        Log.d("CurrentViewModel", "Constructor");
        TransactionRepository transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getActiveTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("CurrentViewModel", "getTransactions()");
        return transactions;
    }
}

