package com.example.customtooldataapp.ui.employeehours;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeHoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeHoursFragment extends Fragment {

    public EmployeeHoursFragment() {
        // Required empty public constructor
    }


    public static EmployeeHoursFragment newInstance(String param1, String param2) {
        EmployeeHoursFragment fragment = new EmployeeHoursFragment();
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
        return inflater.inflate(R.layout.fragment_employee_hours, container, false);
    }
}