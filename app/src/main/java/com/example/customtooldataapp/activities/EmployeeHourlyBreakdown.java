package com.example.customtooldataapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.customtooldataapp.R;

public class EmployeeHourlyBreakdown extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hourly_breakdown);
        //TODO change to employee hours.
        textView.setText("");
    }
}