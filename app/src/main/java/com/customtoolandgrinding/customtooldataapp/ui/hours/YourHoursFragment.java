package com.customtoolandgrinding.customtooldataapp.ui.hours;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.customtoolandgrinding.customtooldataapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import java.util.HashMap;


public class YourHoursFragment extends Fragment {

    private BarChart chart;
    private static final int MAX_X_VALUE = 7;
    private static final String SET_LABEL = "HOURS";
    private static final String[] DAYS = {"", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private HashMap<Integer, Long> thisWeeksHours;
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
        YourHoursViewModel model = new ViewModelProvider(this,
                new YourHoursViewModelFactory(this.getActivity().getApplication()))
                .get(YourHoursViewModel.class);
        Log.d("YourHoursFragment", "OnCreate");
        model.getWeeklyHours().observe(this, weeklyHours -> {
            thisWeeksHours = weeklyHours;
            BarData data = createChartData();
            configureChartAppearance();
            prepareChartData(data);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_hours, container, false);
        chart = view.findViewById(R.id.hours_chart);
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
        for (int i = 1; i <= MAX_X_VALUE; i++) {
            Long y = thisWeeksHours.get(i);
            values.add(new BarEntry(i, y));
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

}