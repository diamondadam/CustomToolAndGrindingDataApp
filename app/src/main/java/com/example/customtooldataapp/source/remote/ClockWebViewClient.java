package com.example.customtooldataapp.source.remote;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ClockWebViewClient extends WebViewClient {
    private final String employeeId;

    public ClockWebViewClient(String employeeId){
        super();
        this.employeeId = employeeId;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        if(url.contains("Default.aspx")){
            Log.d("opStart" , "Default.aspx");

            final String js = "javascript:" +
                    "document.getElementById('MainContent_txtLogin').value = '" + employeeId + "';" +
                    "document.getElementById('MainContent_btnLogin').click()";

            view.evaluateJavascript(js, s -> {});
        }else if (url.contains("Home.aspx")){
            Log.d("opStart" , "Home.aspx");
            final String js = "javascript:" +
                    "document.getElementById('ctl00$MainContent$btnEmpClock').click()";
            view.evaluateJavascript(js, s -> {});
        }
        super.onPageFinished(view, url);
    }
}
