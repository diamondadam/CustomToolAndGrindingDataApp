package com.customtoolandgrinding.customtooldataapp.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;


public class PunchOutService extends Service{
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        final WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        TransactionRepository transactionRepository = TransactionRepository.getInstance(getApplication());
        SharedPreferences sharedPreferences = getSharedPreferences("Employee Identification", MODE_PRIVATE);
        String empID = sharedPreferences.getString("ID", "");

        if(transactionRepository.getActiveTransactions().getValue() == null || transactionRepository.getActiveTransactions().getValue().size() <= 0){
            webView.loadUrl("http://10.10.8.4/dcmobile2/");
            webView.setWebViewClient(new PunchOutWebViewClient(empID));
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

    public class PunchOutWebViewClient extends WebViewClient {
        private final String employeeID;

        public PunchOutWebViewClient(String employeeID){
            super();
            this.employeeID = employeeID;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.contains("Default.aspx")){
                final String js = "javascript:" +
                        "document.getElementById('MainContent_txtLogin').value = '" + employeeID + "';" +
                        "document.getElementById('MainContent_btnLogin').click()";

                view.evaluateJavascript(js, s -> {});

            }else if (url.contains("Home.aspx")){
                final String js = "javascript:" +
                        "document.getElementById('ctl00$MainContent$btnEmpClock').click()";

                view.evaluateJavascript(js, s -> {});
            }
            super.onPageFinished(view, url);
        }
    }
}
