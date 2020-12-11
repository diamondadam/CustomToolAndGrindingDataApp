package com.example.customtooldataapp.ui.transactions.past;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class PastViewModel extends ViewModel {
    private TransactionRepository transactionsRepository;
    private final LiveData<List<Transaction>> transactions;

    public PastViewModel(Application application) {
        Log.d("CurrentViewModel", "Constructor");
        transactionsRepository = new TransactionRepository(application);
        transactions = transactionsRepository.getCurrentTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("CurrentViewModel", "getTransactions()");
        return transactions;
    }
}