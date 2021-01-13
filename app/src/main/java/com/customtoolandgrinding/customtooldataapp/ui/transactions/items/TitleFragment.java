package com.customtoolandgrinding.customtooldataapp.ui.transactions.items;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.ui.transactions.TransactionsFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TitleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TitleFragment extends Fragment implements View.OnClickListener {

    private String jobName;
    private String operationName;
    private String jobId;
    private String operationId;
    private Transaction transaction;

    public TitleFragment() {
        // Required empty public constructor
    }

    public static TitleFragment newInstance(Transaction transaction) {
        TitleFragment fragment = new TitleFragment();
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
        TextView errorText = layout.findViewById(R.id.name_error_text);
        Log.d("Job Name: ", jobName);
        Log.d("Job Id: ", jobId);

        Log.d("Operation Name: ", operationName);
        Log.d("Operation Id: ", operationId);

        if(transaction.getErrorMessage().equals("")){
            jobNameWidget.setText(jobName);
            operationNameWidget.setText(operationName);
            jobIdWidget.setText(jobId);
            operationIdWidget.setText(operationId);
        }else{
            errorText.setText(transaction.getErrorMessage());
        }
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
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(TransactionsFragmentDirections.actionTransactionsFragmentToOperationStopFragment(transaction.getTransactionPath()));
    }
}