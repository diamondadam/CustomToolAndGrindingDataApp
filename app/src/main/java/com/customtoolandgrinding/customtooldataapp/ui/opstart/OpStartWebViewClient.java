package com.customtoolandgrinding.customtooldataapp.ui.opstart;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDeepLinkBuilder;

import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.source.ConnectionError;
import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;
import com.customtoolandgrinding.customtooldataapp.source.remote.JobBossClient;
import com.customtoolandgrinding.customtooldataapp.ui.MainActivity;
import com.customtoolandgrinding.customtooldataapp.ui.opstop.OperationStopFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;

public class OpStartWebViewClient extends WebViewClient {
    private final String operationId;
    private final String employeeID;
    private final Application application;

    public OpStartWebViewClient(String employeeID, String operationId, Application application) {
        super();
        this.application = application;
        this.employeeID = employeeID;
        this.operationId = operationId;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.contains("Default.aspx")) {
            final String js = "javascript:" +
                    "document.getElementById('MainContent_txtLogin').value = '" + employeeID + "';" +
                    "document.getElementById('MainContent_btnLogin').click()";

            view.evaluateJavascript(js, s -> {
            });
        } else if (url.contains("Home.aspx")) {
            final String js = "javascript:" +
                    " document.evaluate( '/html/body/form/div[3]/div/div[2]/ul[1]/li[3]/ul/li[1]/a" +
                    "' ,document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null )" +
                    ".singleNodeValue.click()";

            view.evaluateJavascript(js, s -> {
            });
        } else if (url.contains("OperationStart.aspx")) {

            final String js = "javascript:" +
                    "document.getElementById('txtOpKey_I').value = '" + operationId + "';" +
                    "document.getElementById('MainContent_btnOpStart').click()";

            view.evaluateJavascript(js, s -> {
            });
        }
        super.onPageFinished(view, url);
    }
}
