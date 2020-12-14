package com.example.customtooldataapp.ui.opstart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.customtooldataapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobStartFragment extends Fragment {


    public JobStartFragment() {
        // Required empty public constructor
    }


    public static JobStartFragment newInstance(String param1, String param2) {
        JobStartFragment fragment = new JobStartFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_start, container, false);
        WebView webView = view.findViewById(R.id.webviewOpStart);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        return view;
    }
}