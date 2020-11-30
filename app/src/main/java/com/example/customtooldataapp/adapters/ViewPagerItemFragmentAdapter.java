package com.example.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.fragments.items.BuysFragment;
import com.example.customtooldataapp.fragments.items.NameFragment;
import com.example.customtooldataapp.fragments.NoConnectionFragment;
import com.example.customtooldataapp.fragments.items.PicksFragment;
import com.example.customtooldataapp.fragments.items.QuantitiesFragment;
import com.example.customtooldataapp.fragments.items.TimesFragment;

public class ViewPagerItemFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerItemFragmentAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("ViewPagerFragmentAdapt", "Position: " + position);
        switch (position) {
            case 0:
                return NameFragment.newInstance();
            case 1:
                return QuantitiesFragment.newInstance();
            case 2:
                return BuysFragment.newInstance();
            case 3:
                return PicksFragment.newInstance();
            case 4:
                return TimesFragment.newInstance();
            default:
                return NoConnectionFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
