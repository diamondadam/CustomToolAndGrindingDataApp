package com.example.customtooldataapp.ui.data;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.model.Operation;
import com.example.customtooldataapp.model.Transaction;
import com.example.customtooldataapp.ui.transactions.TransactionsFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameFragment extends Fragment implements View.OnClickListener {

    private String jobName;
    private String operationName;
    private String jobId;
    private String operationId;
    private Transaction transaction;

    public NameFragment() {
        // Required empty public constructor
    }

    public static NameFragment newInstance(Transaction transaction) {
        NameFragment fragment = new NameFragment();
        fragment.transaction = transaction;

        Bundle args = new Bundle();
        args.putString("Job Name", transaction.getJob().getJobName());
        args.putString("Operation Name", transaction.getOperation().getOpName());

        args.putString("JobId", transaction.getJob().getJobId());
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
        View overlay = layout.findViewById(R.id.name_click_overlay);
        TextView jobNameWidget = layout.findViewById(R.id.JobName);
        TextView operationNameWidget = layout.findViewById(R.id.OperationName);
        TextView jobIdWidget = layout.findViewById(R.id.JobId);
        TextView operationIdWidget = layout.findViewById(R.id.OperationId);

        Log.d("Job Name: ", jobName);
        Log.d("Job Id: ", jobId);

        Log.d("Operation Name: ", operationName);
        Log.d("Operation Id: ", operationId);


        jobNameWidget.setText(jobName);
        operationNameWidget.setText(operationName);
        jobIdWidget.setText(jobId);
        operationIdWidget.setText(operationId);

        overlay.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        Log.d("Name Fragment", "OnClick");
        //TODO get employee id from shared prefs
        String empId = "0163";
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStopFragment(empId, transaction.getTransactionPath()));
    }
}