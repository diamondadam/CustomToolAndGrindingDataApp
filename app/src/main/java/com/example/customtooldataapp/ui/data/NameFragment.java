package com.example.customtooldataapp.ui.data;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Operation;
import com.example.customtooldataapp.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameFragment extends Fragment {

    private String jobName;
    private String operationName;
    private String jobId;
    private String operationId;

    public NameFragment() {
        // Required empty public constructor
    }

    public static NameFragment newInstance(Transaction transaction) {
        NameFragment fragment = new NameFragment();
        Bundle args = new Bundle();
        args.putString("Job Name", transaction.getJob().getJobName());
        args.putString("Operation Name", transaction.getOperation().getOpName());
        args.putString("JobId", transaction.getJob().getJobName());
        args.putString("OperationId", transaction.getOperationId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobName = getArguments().getString("Job Name");
            operationName = getArguments().getString("Operation Name");
            jobId = getArguments().getString("JobId");
            operationId = getArguments().getString("OperationId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_name, container, false);
        TextView jobNameWidget = layout.findViewById(R.id.JobName);
        TextView operationNameWidget = layout.findViewById(R.id.OperationName);
        TextView jobIdWidget = layout.findViewById(R.id.JobId);
        TextView operationIdWidget = layout.findViewById(R.id.OperationId);

        Log.d("JobName: ", jobName);
        Log.d("OperationName: ", operationName);

        jobNameWidget.setText(jobName);
        operationNameWidget.setText(operationName);
        jobIdWidget.setText(jobId);
        operationIdWidget.setText(operationId);
        return layout;
    }
}