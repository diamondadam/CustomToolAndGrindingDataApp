package com.customtoolandgrinding.customtooldataapp.ui.opstop;

import android.app.Activity;
import android.app.Application;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.navigation.Navigation;
import com.customtoolandgrinding.customtooldataapp.R;
import com.customtoolandgrinding.customtooldataapp.models.Transaction;
import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;

public class OpStopWebViewClient extends WebViewClient {
    private final Transaction transaction;
    private final String employeeID;
    private String currentUrl;
    private final Application application;
    private final Activity activity;

    public OpStopWebViewClient(String employeeID, Transaction transaction, Activity activity){
        super();
        this.application = activity.getApplication();
        this.activity = activity;
        this.employeeID = employeeID;
        this.transaction = transaction;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if(url.contains("Default.aspx")){
            final String js = "javascript:" +
                    "document.getElementById('MainContent_txtLogin').value = '" + employeeID + "';" +
                    "document.getElementById('MainContent_btnLogin').click()";
            view.evaluateJavascript(js, s -> {});
        }else if (url.contains("Home.aspx")){
            view.loadUrl(currentUrl.replace("Home.aspx", transaction.getTransactionPath()));
        }else if(url.contains(transaction.getTransactionPath())){
            //Fill Layout
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.requestLayout();
        } else if(url.contains("JobEntries.aspx")){
            //updateTransactions();
            Navigation.findNavController(activity, R.id.nav_host_fragment).navigateUp();
        }
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        this.currentUrl = request.getUrl().toString();
        return super.shouldOverrideUrlLoading(view, request);
    }

    private void updateTransactions(){
        TransactionRepository transactionRepository = TransactionRepository.getInstance(application);
        transactionRepository.syncDatabasesThreaded();
    }
}
