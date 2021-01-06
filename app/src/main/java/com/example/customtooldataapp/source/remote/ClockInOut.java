package com.example.customtooldataapp.source.remote;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.customtooldataapp.source.TransactionRepository;


public class ClockInOut extends Service{
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        final WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        TransactionRepository transactionRepository = TransactionRepository.getInstance(getApplication());
        SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
        String empID = sharedPreferences.getString("ID", "");

        if(transactionRepository.getActiveTransactions() == null){
            webView.loadUrl("http://10.10.8.4/dcmobile2/");
            webView.setWebViewClient(new ClockWebViewClient(empID));
        }else{
            Toast.makeText(getApplicationContext(), "Transactions still active!", Toast.LENGTH_LONG).show();
        }
        return flags;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
