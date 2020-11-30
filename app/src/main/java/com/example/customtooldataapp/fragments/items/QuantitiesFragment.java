package com.example.customtooldataapp.fragments.items;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuantitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuantitiesFragment extends Fragment {

    public QuantitiesFragment() {
        // Required empty public constructor
    }

    public static QuantitiesFragment newInstance() {
        QuantitiesFragment fragment = new QuantitiesFragment();/*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
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
        return inflater.inflate(R.layout.fragment_quantities, container, false);
    }
}