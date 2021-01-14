package com.customtoolandgrinding.customtooldataapp.ui.opstop;

import android.app.Application;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.customtoolandgrinding.customtooldataapp.source.TransactionRepository;

public class OpStopWebViewClient extends WebViewClient {
    private final String transactionPath;
    private final String employeeId;
    private String currentUrl;
    private final Application application;


    public OpStopWebViewClient(String employeeId, String transactionPath, Application application){
        super();
        this.application = application;
        this.employeeId = employeeId;
        this.transactionPath = transactionPath;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if(url.contains("Default.aspx")){
            Log.d("opStop" , "Default.aspx");

            final String js = "javascript:" +
                    "document.getElementById('MainContent_txtLogin').value = '" + employeeId + "';" +
                    "document.getElementById('MainContent_btnLogin').click()";
            view.evaluateJavascript(js, s -> {});
        }else if (url.contains("Home.aspx")){
            String destinationUrl = currentUrl.replace("Home.aspx", transactionPath);
            Log.d("opStop" , "Destination...");
            Log.d("opStop" , destinationUrl);
            view.loadUrl(destinationUrl);
        }else if(url.contains(transactionPath)){
            //Fill Layout
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.requestLayout();
        } else if(url.contains("JobEntries.aspx")){
            Log.d("opStop" , "JobEntries.aspx");
            updateTransactions();
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
        transactionRepository.deleteTransactionByPath(transactionPath);
    }
}
