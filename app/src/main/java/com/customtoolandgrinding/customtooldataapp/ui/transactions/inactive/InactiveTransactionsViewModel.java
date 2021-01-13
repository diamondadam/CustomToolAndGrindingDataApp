package com.customtoolandgrinding.customtooldataapp.ui.transactions.inactive;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;

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