package com.example.customtooldataapp.ui.transactions.inactive;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.TransactionRepository;

import java.util.List;

public class InactiveTransactionsViewModel extends ViewModel {
    private TransactionRepository transactionsRepository;
    private final LiveData<List<Transaction>> transactions;

    public InactiveTransactionsViewModel(Application application) {
        transactionsRepository = TransactionRepository.getInstance(application);
        transactions = transactionsRepository.getInactiveTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }
}