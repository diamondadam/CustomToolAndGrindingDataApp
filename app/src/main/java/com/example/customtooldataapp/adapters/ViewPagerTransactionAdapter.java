package com.example.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.ui.error.NoConnectionFragment;
import com.example.customtooldataapp.ui.data.BuysFragment;
import com.example.customtooldataapp.ui.data.NameFragment;
import com.example.customtooldataapp.ui.data.PicksFragment;
import com.example.customtooldataapp.ui.data.QuantitiesFragment;
import com.example.customtooldataapp.ui.data.TimesFragment;

import java.util.List;

public class ViewPagerTransactionAdapter extends FragmentStateAdapter {

    private List<Transaction> transactions;

    public ViewPagerTransactionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Transaction> transactions) {
        super(fragmentManager, lifecycle);
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("VPTA", "createFragment");
        switch (position) {
            case 0:
                Log.d("createFragment", "0");
                return NameFragment.newInstance(transactions.get(position));

            case 1:
                Log.d("createFragment", "1");
                return QuantitiesFragment.newInstance(transactions.get(position));

            case 2:
                Log.d("createFragment", "2");
                return TimesFragment.newInstance();

            case 3:
                Log.d("createFragment", "3");
                return PicksFragment.newInstance();

            case 4:
                Log.d("createFragment", "4");
                return BuysFragment.newInstance();

            default:
                return NoConnectionFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

