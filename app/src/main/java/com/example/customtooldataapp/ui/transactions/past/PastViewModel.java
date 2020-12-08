package com.example.customtooldataapp.ui.transactions.past;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.TransactionsRepository;

import java.util.List;

public class PastViewModel extends ViewModel {

    private TransactionsRepository transactionsRepository;

    public PastViewModel() {
        transactionsRepository = TransactionsRepository.getInstance();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactionsRepository.getTransactions();
    }
}