package com.customtoolandgrinding.customtooldataapp.ui.opstop;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import static android.content.Context.MODE_PRIVATE;

public class OperationStopFragment extends Fragment {
    private String employeeID;
    private Transaction transaction;

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
            transaction = OperationStopFragmentArgs.fromBundle(getArguments()).getTransaction();
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Employee Identification", MODE_PRIVATE);
        employeeID = sharedPreferences.getString("ID", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_operation_stop, container, false);
        WebView webView = view.findViewById(R.id.operation_stop_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        OpStopWebViewClient opStopWebViewClient = new OpStopWebViewClient(employeeID, transaction, getActivity());
        webView.setWebViewClient(opStopWebViewClient);
        return view;
    }
}