package com.example.customtooldataapp.ui.transactions.past;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.source.TransactionsRepository;

import java.util.List;

public class PastViewModel extends ViewModel {
    private final TransactionsRepository transactionsRepository;

    public PastViewModel() {
        Log.d("CurrentViewModel", "Constructor");
        transactionsRepository = TransactionsRepository.getInstance();
    }

    public LiveData<List<Transaction>> getTransactions() {
        Log.d("CurrentViewModel", "getTransactions()");
        if(transactionsRepository.getPastTransactions().getValue() == null){
            Log.d("CurrentViewModel", "Transactions are null...");
        }else{
            Log.d("CurrentViewModel", "Size: " +  transactionsRepository.getPastTransactions().getValue().size());
        }
        return transactionsRepository.getPastTransactions();
    }
}