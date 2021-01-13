package com.customtoolandgrinding.customtooldataapp.ui.hours;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class YourHoursViewModelFactory implements ViewModelProvider.Factory{
    private final Application application;

    public YourHoursViewModelFactory(Application application){
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //return (T) new MyViewModel(mApplication, mParam);
        return (T) new YourHoursViewModel(application);
    }
}
