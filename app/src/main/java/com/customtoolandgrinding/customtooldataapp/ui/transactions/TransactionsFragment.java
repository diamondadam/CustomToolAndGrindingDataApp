package com.customtoolandgrinding.customtooldataapp.ui.transactions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.adapters.TabPagerAdapter;
import com.customtoolandgrinding.customtooldataapp.ui.MainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {
    private final String[] tabTitles = {"Current", "Past"};

    public TransactionsFragment() {
        // Required empty public constructor
    }

    public static TransactionsFragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_transactions, container, false);
        //ViewPager2 Initialization
        ViewPager2 viewPager2 = layout.findViewById(R.id.viewpager2);
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new MarginPageTransformer(1500));
        viewPager2.setUserInputEnabled(false);

        TabLayout tabLayout = layout.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //TODO ((MainActivity) getActivity()).startSync();
        }catch(Exception e){
            //Do Nothing
        }

    }
}