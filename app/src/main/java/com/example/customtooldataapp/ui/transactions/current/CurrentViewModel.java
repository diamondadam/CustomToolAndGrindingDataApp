package com.example.customtooldataapp.ui.transactions.current;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.TransactionsRepository;

import java.util.List;

public class CurrentViewModel extends ViewModel {

    private final TransactionsRepository transactionsRepository;

    public CurrentViewModel() {
        Log.d("CurrentViewModel", "Constructor");
        transactionsRepository = TransactionsRepository.getInstance();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("CurrentViewModel", "getTransactions()");
        if(transactionsRepository.getTransactions().getValue() == null){
            Log.d("CurrentViewModel", "Transactions are null...");
        }else{
            Log.d("CurrentViewModel", "Size: " +  transactionsRepository.getTransactions().getValue().size());
        }
        return transactionsRepository.getTransactions();
    }
}

