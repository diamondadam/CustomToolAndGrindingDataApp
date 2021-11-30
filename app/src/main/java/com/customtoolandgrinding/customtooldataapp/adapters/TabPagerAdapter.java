package com.customtoolandgrinding.customtooldataapp.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.active.ActiveTransactionsFragment;

import com.customtoolandgrinding.customtooldataapp.ui.transactions.inactive.InactiveTransactionsFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return ActiveTransactionsFragment.newInstance();
            case 1:
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
