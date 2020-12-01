package com.example.customtooldataapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.fragments.NoConnectionFragment;
import com.example.customtooldataapp.fragments.items.BuysFragment;
import com.example.customtooldataapp.fragments.items.NameFragment;
import com.example.customtooldataapp.fragments.items.PicksFragment;
import com.example.customtooldataapp.fragments.items.QuantitiesFragment;
import com.example.customtooldataapp.fragments.items.TimesFragment;

public class ViewPagerTransactionAdapter extends FragmentStateAdapter {

    public ViewPagerTransactionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("VPTA", "createFragment");
        switch (position) {
            case 0:
                Log.d("createFragment", "0");
                return NameFragment.newInstance();

            case 1:
                Log.d("createFragment", "1");
                return QuantitiesFragment.newInstance();

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

