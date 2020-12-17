package com.example.customtooldataapp.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.customtooldataapp.ui.transactions.current.CurrentFragment;
import com.example.customtooldataapp.ui.error.NoConnectionFragment;
import com.example.customtooldataapp.ui.transactions.past.PastFragment;

public class TabFragmentAdapter extends FragmentStateAdapter {

    public TabFragmentAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("ViewPagerFragmentAdapt", "Position: " + position);
        switch (position) {
            case 0:
                Log.d("ViewPagerFragmentAdapt", "CurrentFragment: ");
                return CurrentFragment.newInstance();
            case 1:
                Log.d("ViewPagerFragmentAdapt", "Past Fragment");
                return PastFragment.newInstance();
            default:
                return NoConnectionFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
