package com.example.customtooldataapp.ui.data;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimesFragment extends Fragment {

    private String remainingSetup;
    private String remainingRuntime;

    private String setup;
    private String runtime;

    public TimesFragment() {
        // Required empty public constructor
    }

    public static TimesFragment newInstance(Transaction transaction) {
        TimesFragment fragment = new TimesFragment();
        Bundle args = new Bundle();

        args.putString("Remaining Setup", String.valueOf(transaction.getOperation().getRemainingSetupTime()));
        args.putString("Remaining Run", String.valueOf(transaction.getOperation().getRemainingRuntime()));

        args.putString("Setup", String.valueOf(transaction.getOperation().getSetupTime()));
        args.putString("Run", String.valueOf(transaction.getOperation().getRuntime()));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.remainingRuntime = getArguments().getString("Remaining Setup");
            this.remainingSetup = getArguments().getString("Remaining Run");
            this.setup = getArguments().getString("Setup");
            this.runtime = getArguments().getString("Run");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_times, container, false);

        TextView rSetup = layout.findViewById(R.id.remaining_setup);
        TextView rRuntime = layout.findViewById(R.id.remaining_run);
        TextView eSetup = layout.findViewById(R.id.setup_time);
        TextView eRuntime = layout.findViewById(R.id.run_time);

        rSetup.setText(String.format("Remaining Setup: %s", remainingSetup));
        rRuntime.setText(String.format("Remaining Runtime: %s", remainingRuntime));
        eSetup.setText(String.format("Estimated Setup: %s", setup));
        eRuntime.setText(String.format("Estimated Runtime: %s", runtime));
        AlphaAnimation buttonClick = new AlphaAnimation(5.0F, 0.25F);
        buttonClick.setDuration(300);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClick", "Onclick!");
            }
        });
        return layout;
    }
}