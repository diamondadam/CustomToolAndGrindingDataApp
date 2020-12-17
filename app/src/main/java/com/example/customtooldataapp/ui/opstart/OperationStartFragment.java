package com.example.customtooldataapp.ui.opstart;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.customtooldataapp.R;
import com.example.customtooldataapp.ui.opstop.OperationStopFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperationStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationStartFragment extends Fragment {
    private static final String EMP_ID = "Employee Id";
    private static final String OPERATION_ID = "Operation Id";

    private String employeeId;
    private String operationId;

    public OperationStartFragment() {
        // Required empty public constructor
    }

    public static OperationStartFragment newInstance() {
        return new OperationStartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employeeId = OperationStartFragmentArgs.fromBundle(getArguments()).getEmployeeId();
            Log.d("OpStartFragment", "onCreate()");
            Log.d("OpStartFragment", employeeId);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_operation_start, container, false);
        WebView webView = view.findViewById(R.id.operation_start_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        OpStartWebViewClient opStartWebViewClient = new OpStartWebViewClient(employeeId, operationId);
        webView.setWebViewClient(opStartWebViewClient);
        return view;
    }
}