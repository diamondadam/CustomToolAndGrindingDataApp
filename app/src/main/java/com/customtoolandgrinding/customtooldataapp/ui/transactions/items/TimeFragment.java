package com.customtoolandgrinding.customtooldataapp.ui.transactions.items;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeFragment extends Fragment implements View.OnClickListener {

    private String remainingSetup;
    private String remainingRuntime;

    private String setup;
    private String runtime;

    public TimeFragment() {
        // Required empty public constructor
    }

    public static TimeFragment newInstance(Transaction transaction) {
        Log.d("TimeFrag newInstance()", "Here");
        TimeFragment fragment = new TimeFragment();
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
        View overlay = layout.findViewById(R.id.times_click_overlay);


        TextView rSetup = layout.findViewById(R.id.remaining_setup);
        TextView rRuntime = layout.findViewById(R.id.remaining_run);
        TextView eSetup = layout.findViewById(R.id.setup_time);
        TextView eRuntime = layout.findViewById(R.id.run_time);

        rSetup.setText(String.format("Remaining Setup: %s", remainingSetup));
        rRuntime.setText(String.format("Remaining Runtime: %s", remainingRuntime));
        eSetup.setText(String.format("Estimated Setup: %s", setup));
        eRuntime.setText(String.format("Estimated Runtime: %s", runtime));

        overlay.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        Log.d("Times Fragment", "OnClick");
    }
}