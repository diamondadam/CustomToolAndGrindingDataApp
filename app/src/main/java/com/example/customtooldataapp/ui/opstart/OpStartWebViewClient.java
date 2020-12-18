package com.example.customtooldataapp.ui.opstart;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpStartWebViewClient extends WebViewClient {
    private final String operationId;
    private final String employeeId;

    public OpStartWebViewClient(String employeeId, String operationId){
        super();

        this.employeeId = employeeId;
        this.operationId = operationId;
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
                    " document.evaluate( '/html/body/form/div[3]/div/div[2]/ul[1]/li[3]/ul/li[1]/a" +
                    "' ,document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null )" +
                    ".singleNodeValue.click()";

            view.evaluateJavascript(js, s -> {});
        }else if(url.contains("OperationStart.aspx")){
            Log.d("opStart" , "OperationStart.aspx");

            final String js = "javascript:" +
                    "document.getElementById('txtOpKey_I').value = '" + operationId + "';" +
                    "document.getElementById('MainContent_btnOpStart').click()";

            view.evaluateJavascript(js, s -> {});
        }

        super.onPageFinished(view, url);
    }

    private void opStart(){

    }

    private void opStop(){

    }
}
