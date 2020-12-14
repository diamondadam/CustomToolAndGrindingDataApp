package com.example.customtooldataapp.ui.transactions.current;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.customtooldataapp.ui.transactions.current.CurrentViewModel;

public class CurrentViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public CurrentViewModelFactory(Application application) {
        this.application = application;
   }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //return (T) new MyViewModel(mApplication, mParam);
        return (T) new CurrentViewModel(application);
    }
}
