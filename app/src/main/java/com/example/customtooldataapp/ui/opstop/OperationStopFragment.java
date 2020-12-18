package com.example.customtooldataapp.ui.opstop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.customtooldataapp.R;

public class OperationStopFragment extends Fragment {
    private String employeeId;
    private String transactionPath;

    public OperationStopFragment() {
        // Required empty public constructor
    }

    public static OperationStopFragment newInstance() {
        return new OperationStopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employeeId = OperationStopFragmentArgs.fromBundle(getArguments()).getEmployeeId();
            transactionPath = OperationStopFragmentArgs.fromBundle(getArguments()).getTransactionPath();
            Log.d("OpStopFragment", "onCreate() getting data...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_operation_stop, container, false);
        WebView webView = view.findViewById(R.id.operation_stop_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        OpStopWebViewClient opStopWebViewClient = new OpStopWebViewClient(employeeId, transactionPath);
        webView.setWebViewClient(opStopWebViewClient);
        return view;
    }
}