package com.example.customtooldataapp.ui.hours;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customtooldataapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import java.util.Random;


public class YourHoursFragment extends Fragment {

    private BarChart chart;
    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 24;
    private static final int MIN_Y_VALUE = 0;
    private static final String SET_LABEL = "HOURS";
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

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
        View view = inflater.inflate(R.layout.fragment_your_hours, container, false);

        chart = view.findViewById(R.id.hours_chart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        return view;
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            float y = new Util().randomFloatBetween(MIN_Y_VALUE, MAX_Y_VALUE);
            values.add(new BarEntry(x, y));
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

    public class Util {
        public float randomFloatBetween(float min, float max) {
            Random r = new Random();
            float random = min + r.nextFloat() * (max - min);
            return random;
        }
    }
}