package com.example.customtooldataapp.ui.hours;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;


public class YourHoursFragment extends Fragment {


    public YourHoursFragment() {
        // Required empty public constructor
    }


    public static YourHoursFragment newInstance(String param1, String param2) {
        YourHoursFragment fragment = new YourHoursFragment();

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
        return inflater.inflate(R.layout.fragment_your_hours, container, false);
    }
}