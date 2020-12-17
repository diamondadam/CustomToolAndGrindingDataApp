package com.example.customtooldataapp.ui.opstop;

import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpStopWebViewClient extends WebViewClient {
    private final String transactionPath;
    private final String employeeId;
    private String currentUrl;

    public OpStopWebViewClient(String employeeId, String transactionPath){
        super();

        this.employeeId = employeeId;
        this.transactionPath = transactionPath;
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

            String destinationUrl = currentUrl.replace("Home.aspx", transactionPath);

            Log.d("opStart" , "Destination...");
            Log.d("opStart" , destinationUrl);

            view.loadUrl(destinationUrl);



        }else if(url.contains(transactionPath)){
            //Fill Layout
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.requestLayout();
        } else if(url.contains("JobEntries.aspx")){
            Log.d("opStop" , "JobEntries.aspx");
            view.destroy();
        }

        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        this.currentUrl = request.getUrl().toString();
        return super.shouldOverrideUrlLoading(view, request);
    }
}
