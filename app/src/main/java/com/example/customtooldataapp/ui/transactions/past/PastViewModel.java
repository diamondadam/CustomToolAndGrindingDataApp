package com.example.customtooldataapp.ui.transactions.past;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.data.model.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class PastViewModel extends ViewModel {
    private TransactionRepository transactionsRepository;
    private final LiveData<List<Transaction>> transactions;

    public PastViewModel(Application application) {
        transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getPastTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }
}