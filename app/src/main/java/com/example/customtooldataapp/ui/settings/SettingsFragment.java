package com.example.customtooldataapp.ui.settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import com.example.customtooldataapp.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    private boolean autoSetup;
    private boolean quickPunch;
    private static final String AUTO_SETUP = "Auto Setup";
    private static final String QUICK_PUNCH = "Quick Punch";

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(Application app) {
        SettingsFragment fragment = new SettingsFragment();
        SharedPreferences sharedPreferences = app.getSharedPreferences("Settings", MODE_PRIVATE);

        Bundle args = new Bundle();

        args.putBoolean(AUTO_SETUP, sharedPreferences.getBoolean(AUTO_SETUP, false));
        args.putBoolean(QUICK_PUNCH, sharedPreferences.getBoolean(QUICK_PUNCH, false));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            autoSetup = getArguments().getBoolean(AUTO_SETUP);
            quickPunch = getArguments().getBoolean(QUICK_PUNCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        SwitchCompat autoSetupSwitch = layout.findViewById(R.id.auto_setup_switch);
        autoSetupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(AUTO_SETUP, String.valueOf(isChecked));
                editor.putBoolean(AUTO_SETUP, isChecked);

            }
        });

        SwitchCompat quickPunchSwitch = layout.findViewById(R.id.quick_punch_switch);
        quickPunchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(QUICK_PUNCH, String.valueOf(isChecked));
                editor.putBoolean(QUICK_PUNCH, isChecked);
            }
        });

        return layout;
    }
}