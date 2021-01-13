package com.customtoolandgrinding.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.items.TitleFragment;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.items.QuantityFragment;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.items.TimeFragment;

public class DataPagerAdapter extends FragmentStateAdapter {

    private final Transaction transaction;

    public DataPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Transaction transaction) {
        super(fragmentManager, lifecycle);
        Log.d("DataFragmentAdapter", "constuctor");
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("DataFragmentAdapter", "createFragment");

        switch (position) {
            case 0:
                Log.d("DataFragmentAdapter", "0");
                return TitleFragment.newInstance(transaction);

            case 1:
                Log.d("DataFragmentAdapter", "1");
                return QuantityFragment.newInstance(transaction);

            case 2:
                Log.d("DataFragmentAdapter", "2");
                return TimeFragment.newInstance(transaction);

            default:
                return TitleFragment.newInstance(transaction);
        }

    }

    @Override
    public int getItemCount() {
        if(transaction.getErrorMessage().equals("")){
            return 3;
        }else{
            return 1;
        }

    }

}

