package com.example.customtooldataapp.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.customtooldataapp.ui.opstart.OpStartWebViewClient;

public class WebService extends Service {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        final WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://10.10.8.4/dcmobile2/");
        webView.setWebViewClient(new OpStartWebViewClient("", ""));
        return flags;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}