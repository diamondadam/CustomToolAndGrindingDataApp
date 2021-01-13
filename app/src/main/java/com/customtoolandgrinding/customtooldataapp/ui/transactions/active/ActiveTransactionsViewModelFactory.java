package com.customtoolandgrinding.customtooldataapp.ui.transactions.active;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ActiveTransactionsViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ActiveTransactionsViewModelFactory(Application application) {
        this.application = application;
   }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //return (T) new MyViewModel(mApplication, mParam);
        return (T) new ActiveTransactionsViewModel(application);
    }
}
