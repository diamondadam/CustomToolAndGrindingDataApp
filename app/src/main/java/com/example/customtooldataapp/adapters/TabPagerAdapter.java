package com.example.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.ui.transactions.active.ActiveTransactionsFragment;

import com.example.customtooldataapp.ui.transactions.inactive.InactiveTransactionsFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("ViewPagerFragmentAdapt", "Position: " + position);
        switch (position) {
            case 0:
                Log.d("ViewPagerFragmentAdapt", "CurrentFragment: ");
                return ActiveTransactionsFragment.newInstance();
            case 1:
                Log.d("ViewPagerFragmentAdapt", "Past Fragment");
                return InactiveTransactionsFragment.newInstance();
            default:
                return ActiveTransactionsFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
