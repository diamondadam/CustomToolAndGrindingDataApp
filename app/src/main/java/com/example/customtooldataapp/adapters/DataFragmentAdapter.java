package com.example.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.data.model.Transaction;
import com.example.customtooldataapp.ui.transactions.itemfragments.NameFragment;
import com.example.customtooldataapp.ui.transactions.itemfragments.QuantitiesFragment;
import com.example.customtooldataapp.ui.transactions.itemfragments.TimesFragment;

public class DataFragmentAdapter extends FragmentStateAdapter {

    private final Transaction transaction;

    public DataFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Transaction transaction) {
        super(fragmentManager, lifecycle);
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("DataFragmentAdapter", "createFragment");
        switch (position) {
            case 0:
                Log.d("DataFragmentAdapter", "0");
                return NameFragment.newInstance(transaction);

            case 1:
                Log.d("DataFragmentAdapter", "1");
                return QuantitiesFragment.newInstance(transaction);

            case 2:
                Log.d("DataFragmentAdapter", "2");
                return TimesFragment.newInstance(transaction);

            default:
                return NameFragment.newInstance(transaction);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}

