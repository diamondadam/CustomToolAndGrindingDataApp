package com.example.customtooldataapp.source.remote;

import android.util.Log;
import android.util.Pair;

import com.example.customtooldataapp.data.model.Job;
import com.example.customtooldataapp.data.model.Operation;
import com.example.customtooldataapp.data.model.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JobBossClient {
    private static final String UTF_8 = "UTF-8";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36";
    private static final String HOME = "/Home.aspx";
    private static final String DEFAULT = "/Default.aspx";
    private static final String JOB_ENTRIES = "/JobEntries.aspx";
    private static final String JOB_DETAILS = "/JobDetails.aspx";
    private List<HttpCookie> cookieList;
    private HttpURLConnection conn;
    private String urlBase = "http://10.10.8.4/dcmobile2/";

    private String employeeId;

    public JobBossClient(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Transaction> getTransactions() {
        //Initialize login
        initLogin();

        //Initialize Arraylist
        List<Transaction> transactions = new ArrayList<>();
        //Initialize Jsoup Document
        Document document;
        Pair<Job, Operation> pair;

        //Try to get JobEntries page
        try {
            document = Jsoup.parse(getPageContent(JOB_ENTRIES));
        } catch (Exception e) {
            Log.d("getTransactions", e.toString());
            return transactions;
        }
        //Break it up by transactions
        Elements links = document.getElementsByAttribute("href");

        //For each transaction
        for (Element elem : links) {
            // Move through all the links and find the entry routes
            String link = elem.attr("href");
            if (link.contains("OpStop.aspx")) {
                //Set jobId and create Transaction
                String jobId = elem.text();
                Transaction singleTransaction = new Transaction(link);
                String operationId = singleTransaction.getOperationId();


                //Try to get job data returns if there is an error
                try {
                    pair = getJobData(getPageContent(JOB_DETAILS.concat("?id=" + elem.text())), jobId, operationId);
                    // Get job data
                    singleTransaction.setJob(pair.first);
                    singleTransaction.setOperation(pair.second);

                    transactions.add(singleTransaction);
                } catch (Exception e) {
                    Log.d("getTransactions", e.toString());
                    return transactions;
                }


            }
        }

        Log.d("JBC getTransactions()", "Returning transactions...");
        Log.d("JBC getTransactions()", "Size: " + transactions.size());

        return transactions;
    }

    /**
     * Returns a String containing the page html.
     */
    private String getPageContent(String url_ext) throws Exception {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        CookieStore cookieStore = cookieManager.getCookieStore();

        URL obj = new URL(urlBase.concat(url_ext));

        conn = (HttpURLConnection) obj.openConnection();

        conn.setInstanceFollowRedirects(true);
        conn.setUseCaches(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-alive");
        conn.setRequestProperty("Origin", "http://10.10.8.4");
        conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.setRequestProperty("DNT", "1");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        conn.setRequestProperty("Referer", urlBase.concat(url_ext));
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");

        if (!(cookieList == null)) {
            for (HttpCookie cookie : cookieList) {
                conn.setRequestProperty("Cookie", cookie.toString());
            }
        }

        System.out.println("\nSending 'GET' request to URL : " + urlBase.concat(url_ext));
        System.out.println("Response Code : " + conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        setSessionId(conn.getURL().toString());

        // Get the response cookies
        cookieList = cookieStore.getCookies();
        return response.toString();
    }

    /**
     * Sets the session ID in the base url.
     */
    private void setSessionId(String url) {
        if (urlBase.equals("http://10.10.8.4/dcmobile2/")) {
            urlBase =
                    urlBase.concat(
                            url.replace("http://10.10.8.4/dcmobile2/", "").replaceAll("/Default.aspx", ""));
        }
    }

    private void initLogin() {

        //Establish initialize connection, sets session id and cookies.
        Document document;

        try {
            document = Jsoup.parse(getPageContent(DEFAULT));
        } catch (Exception e) {
            Log.d("initLogin", e.toString());
            return;
        }


        Elements inputElements = document.getElementsByTag("input");

        String viewSt = null;
        String viewGen = null;
        String eventVal = null;

        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");
            switch (key) {
                case "__VIEWSTATE":
                    viewSt = value;
                    break;

                case "__VIEWSTATEGENERATOR":
                    viewGen = value;
                    break;

                case "__EVENTVALIDATION":
                    eventVal = value;
                    break;
            }
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("__LASTFOCUS", "")
                .addFormDataPart("__EVENTTARGET", "")
                .addFormDataPart("__EVENTARGUMENT", "")
                .addFormDataPart("__VIEWSTATE", viewSt)
                .addFormDataPart("__VIEWSTATEGENERATOR", viewGen)
                .addFormDataPart("__EVENTVALIDATION", eventVal)
                .addFormDataPart("ctl00$MainContent$txtLogin", "0163")
                .addFormDataPart("ctl00$MainContent$btnLogin", "Log In")
                .addFormDataPart("ctl00$TimoutControl1$TimeoutPopupState", "{&quot;windowsState&quot;:&quot;0:0:-1:0:0:0:-10000:-10000:1:0:0:0&quot;}")
                .addFormDataPart("DXScript", "1_258,1_139,1_252,1_165,1_142,1_136,1_244,1_242,1_152,1_185,1_137")
                .addFormDataPart("DXCss", "0_1239%2C1_29%2C1_32%2C1_30%2C0_1241%2C1_9%2C1_10%2C1_8%2Chttp%3A%2F%2Fgc.kis.v2.scr.kaspersky-labs.com%2FE3E8934C-235A-4B0E-825A-35A08381A191%2Fabn%2Fmain.css%3Fattr%3DaHR0cDovLzEwLjEwLjguNC9kY21vYmlsZTIvKFMobmk0c2pjdmVwZWhkajBrMWF4c3kzd2cyKSkvRGVmYXVsdC5hc3B4%2C%2FDCMobile2%2FContent%2Fbootstrap.css%2C%2FDCMobile2%2FContent%2FSite.css%2Cfavicon.ico")
                .build();

        Request request = new Request.Builder()
                .url(urlBase + DEFAULT)
                .post(requestBody)
                .addHeader("Cookie", String.valueOf(cookieList.get(0)))
                .build();

        try {
            client.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            Log.d("initLogin", e.toString());
        }
    }

    private Pair<Job, Operation> getJobData(String html, String jobId, String operationId) {

        Job job = new Job(jobId);
        Document doc = Jsoup.parse(html);

        Elements jobData = doc.getElementById("JobData").getElementsByTag("span");
        Elements qtyData = doc.getElementById("QtyData").getElementsByTag("span");
        Elements customerData = doc.getElementById("CustData").getElementsByTag("span");

        Elements routes =
                doc.getElementById("grdRouting_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdRouting_DXDataRow."));
        Elements picks =
                doc.getElementById("grdPick_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdPick_DXDataRow."));
        Elements buys =
                doc.getElementById("grdBuy_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdBuy_DXDataRow."));

        parseJobData(job, jobData);
        parseQtyData(job, qtyData);
        parseCustomerData(job, customerData);

        Operation operation = new Operation(operationId);

        if (routes.size() > 0) {
            for (Element elem : routes) {
                //Check for the transactions operation ONLY
                if (elem.getElementsByTag("td").get(0).text().equals(operationId)) {
                    parseRoutes(elem.getElementsByTag("td"), operation);
                }
            }
        }
        if (picks.size() > 0) {
            try {
                parsePicks(job, picks.get(0).getElementsByTag("td"));
            } catch (Exception e) {
                Log.d("JBC", e.toString());
            }

        }
        if (buys.size() > 0) {
            parseBuys(job, buys.get(0).getElementsByTag("td"));
        }
        return new Pair<>(job, operation);
    }

    private void parseJobData(Job job, Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            //System.out.println(elements.get(i).text());
            switch (i) {
                case 1:
                    job.setOrderDate(elements.get(i).text());
                case 3:
                    job.setNextDeliveryDate(elements.get(i).text());
                case 5:
                    job.setJobName(elements.get(i).text());
                case 7:
                    job.setDescription(elements.get(i).text());
                case 9:
                    job.setRevision(elements.get(i).text());
            }
        }
    }

    private void parseQtyData(Job job, Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            //System.out.println(elements.get(i).text());

            switch (i) {
                case 1:
                    job.setOrderQty(Integer.parseInt(elements.get(i).text()));
                    break;

                case 3:
                    job.setMakeQty(Integer.parseInt(elements.get(i).text()));
                    break;

                case 5:
                    job.setPickQty(Integer.parseInt(elements.get(i).text()));
                    break;

                case 7:
                    job.setInProductionQty(Integer.parseInt(elements.get(i).text()));
                    break;

                case 9:
                    job.setCompletedQty(Integer.parseInt(elements.get(i).text()));
                    break;

                case 11:
                    job.setShippedQty(Integer.parseInt(elements.get(i).text()));
                    break;

            }
        }
    }

    private void parseCustomerData(Job job, Elements elements) {

        for (int i = 0; i < elements.size(); i++) {
            switch (i) {
                case 1:
                    job.setCustomer(elements.get(i).text());
                    break;

                case 3:
                    job.setPurchaseOrder(elements.get(i).text());
                    break;

                case 5:
                    job.setPoLine(elements.get(i).text());
                    break;

                case 7:
                    job.setShipTo(elements.get(i).text());
                    break;

                case 9:
                    job.setAddress(elements.get(i).text());
                    break;

            }
        }
    }

    private Operation parseRoutes(Elements elements, Operation op) {
        for (int i = 0; i < elements.size(); i++) {
            switch (i) {
                case 0:
                    op.setOperationNumber(elements.get(i).text());
                    break;
                case 1:
                    op.setWcVendor(elements.get(i).text());
                    break;

                case 2:
                    op.setOpName(elements.get(i).text());
                    break;

                case 3:
                    op.setOperationDescription(elements.get(i).text());
                    break;

                case 4:
                    op.setScheduledStart(elements.get(i).text());
                    break;

                case 5:
                    op.setQtyCompleted(Integer.parseInt(elements.get(i).text()));
                    break;

                case 6:
                    op.setRemainingRuntime(Float.parseFloat(elements.get(i).text()));
                    break;

                case 7:
                    op.setRemainingSetupTime(Float.parseFloat(elements.get(i).text()));
                    break;

                case 8:
                    op.setStatus(elements.get(i).text());
                    break;

                case 9:
                    op.setSetupTime(Float.parseFloat(elements.get(i).text()));
                    break;

                case 10:
                    op.setRuntime(Float.parseFloat(elements.get(i).text()));
                    break;

                case 11:
                    op.setRunMethod(elements.get(i).text());
                    break;

            }
        }
        return op;
    }

    private void parsePicks(Job job, Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            switch (i) {
                case 0:
                    job.setMaterialRequisition(elements.get(i).text());
                    break;
                case 1:
                    job.setMaterial(elements.get(i).text());
                    break;
                case 2:
                    job.setPickDescription(elements.get(i).text());
                    break;
                case 3:
                    job.setQtyRequired(Integer.parseInt(elements.get(i).text()));
                    break;
                case 4:
                    job.setPickQty(Integer.parseInt(elements.get(i).text()));
                    break;
                case 5:
                    job.setPickStatus(elements.get(i).text());
                    break;
                case 6:
                    job.setNotes(elements.get(i).text());
                    break;
            }
        }
    }

    private void parseBuys(Job job, Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            System.out.println(elements.get(i).text());
            switch (i) {
                case 0:
                    job.setMaterialRequisition(elements.get(i).text());
                    break;
                case 1:
                    job.setMaterial(elements.get(i).text());
                    break;
                case 2:
                    job.setPickDescription(elements.get(i).text());
                    break;
                case 3:
                    job.setQtyRequired(Integer.parseInt(elements.get(i).text()));
                    break;
                case 4:
                    job.setPickQty(Integer.parseInt(elements.get(i).text()));
                    break;
                case 5:
                    job.setPickStatus(elements.get(i).text());
                    break;
                case 6:
                    job.setNotes(elements.get(i).text());
                    break;
            }
        }
    }
}