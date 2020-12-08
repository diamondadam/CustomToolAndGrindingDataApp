package com.example.customtooldataapp.source.remote;

import android.util.Log;

import com.example.customtooldataapp.model.Employee;
import com.example.customtooldataapp.model.Job;
import com.example.customtooldataapp.model.Operation;
import com.example.customtooldataapp.model.StopForm;
import com.example.customtooldataapp.model.Transaction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//TODO: Fix POST Requests/MOVE to okhttp
public class JobBossClient {
    private static final String UTF_8 = "UTF-8";
    private List<HttpCookie> cookieList;
    private HttpURLConnection conn;

    private String urlBase = "http://10.10.8.4/dcmobile2/";

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36";
    private static final String HOME = "/Home.aspx";
    private static final String DEFAULT = "/Default.aspx";
    private static final String JOB_ENTRIES = "/JobEntries.aspx";
    private static final String GROUP_ENTRIES = "/GroupEntries.aspx";
    private static final String OPERATION_START = "/OperationStart.aspx";
    private static final String JOB_START = "/JobStart.aspx";
    private static final String GROUP_START = "/GroupStart.aspx";
    private static final String ELAPSED_TIME = "/ElapsedTime.aspx";
    private static final String ELAPSED_TIME_BY_JOB = "ElapsedTimebyJob.aspx";
    private static final String ISSUE_REQ = "/IssueReq.aspx";
    private static final String PICK_BY_JOB = "/PickByJob.aspx";
    private static final String JOB_DETAILS = "/JobDetails.aspx";
    private static final String OP_STOP = "OpStop.aspx";

    private static JobBossClient instance;
    private static Employee employee;

    public static JobBossClient getInstance() {
        if (instance == null) {
            instance = new JobBossClient();
        }
        return instance;
    }

    private JobBossClient() {
        Log.d("JBC Constructor", "Starting...");
        employee = Employee.getInstance();

    }


    public List<Transaction> getTransactions() {
        try{
            init(employee);
            return instance.getJobData(employee);
        }catch (Exception e){
            Log.d("JBC getTransactions", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * This method establishes the initial connection with the server.
     */

    private void init(Employee emp) {
        // Establishes session and verifies user.
        try {
            Log.d("JBC Init", "Starting...");
            postRequest(defaultFormParams(getPageContent(""), emp), DEFAULT);
        }catch (Exception e){
            Log.d("JBC Init", e.toString());
        }

    }

    /**
     * Returns a String containing the page html.
     */
    public String getPageContent(String url_ext) throws Exception {
        Log.d("JBC getPageContent", "Starting...");
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

        Log.d("JBC getPageContent", "Sending 'GET' request to URL : " + urlBase.concat(url_ext));
        Log.d("JBC getPageContent", String.valueOf(conn.getResponseCode()));
        Log.d("JBC getPageContent", String.valueOf(conn.getURL()));

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

    private String postRequest(String formParams, String path) throws IOException {
        URL obj = new URL(urlBase.concat(path));
        conn = (HttpURLConnection) obj.openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setUseCaches(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-alive");
        conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.setRequestProperty("DNT", "1");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        conn.setRequestProperty("Referer", urlBase.concat(path));
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        conn.setRequestProperty("Content-Length", Integer.toString(formParams.length()));

        if (!(cookieList == null)) {
            for (HttpCookie cookie : cookieList) {
                conn.setRequestProperty("Cookie", cookie.toString());
            }
        }

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(formParams);
        wr.flush();
        wr.close();

        System.out.println("\nSending 'POST' request to URL : " + urlBase + path);
        System.out.println("Current URL: " + conn.getURL());

        //System.out.println("Response Code : " + conn.getResponseCode());
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            // System.out.println(inputLine);
        }
        in.close();
        return response.toString();
    }

    /**
     * Obtains the entry parameters, and job identification number
     */
    private List<Transaction> getJobData(Employee emp) throws Exception {

        Document doc = Jsoup.parse(getPageContent(JOB_ENTRIES));
        Elements links = doc.getElementsByAttribute("href");
        List<Transaction> transactions = new ArrayList<>();
        for (Element elem : links) {
            // Move through all the links and find the entry routes
            String link = elem.attr("href");
            if (link.contains("OpStop.aspx")) {
                // Get job data
                transactions.add(new Transaction(link, elem.text()));
                getJob(getPageContent(JOB_DETAILS.concat("?id=" + elem.text())), emp, elem.text());
            }
        }
        return transactions;
    }

    public void getJob(String html, Employee emp, String jobId) throws ParseException {

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

        if (routes.size() > 0) {
            for (Element elem : routes) {
                parseRoutes(job, elem.getElementsByTag("td"));
            }
        }
        if (picks.size() > 0) {
            //parsePicks(job, picks.get(0).getElementsByTag("td"));
        }
        if (buys.size() > 0) {
            parseBuys(job, buys.get(0).getElementsByTag("td"));
        }
        emp.addJob(job);
    }

    /**
     * Sets the session ID in the base url.
     */
    public void setSessionId(String url) {
        if (urlBase.equals("http://10.10.8.4/dcmobile2/")) {
            urlBase =
                    urlBase.concat(
                            url.replace("http://10.10.8.4/dcmobile2/", "").replaceAll("/Default.aspx", ""));
        }
    }

    // TODO: test opStart
    public String opStart(Employee emp, String operationId) throws Exception {
        return postRequest(
                startFormParams(getPageContent(OPERATION_START), emp, operationId), OPERATION_START);
    }

    // TODO: Fix opStop
    public String opStop(Transaction transaction, StopForm form) throws Exception {
        return postRequest(stopFormParams(getPageContent("/" + transaction.getTransactionPath()), form), "/" + transaction.getTransactionPath());
    }

    public String clockInAndOut(Employee emp) throws Exception {
        return postRequest(defaultFormParams(getPageContent(HOME), emp), HOME);
    }

    /**
     * Grabs the form input names and values from the DEFAULT page.
     */
    private String defaultFormParams(String html, Employee emp) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(html);

        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<>();

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
            Response response = client.newCall(request).execute();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    private String startFormParams(String html, Employee emp, String operationId) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(html);

        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<>();

        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");
            switch (key) {
                case "ctl00$MainContent$txtOpKey":
                    value = operationId;
                    paramList.add(URLEncoder.encode(key, UTF_8) + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$ctl01$hiddenIsTabChange":
                    paramList.add(URLEncoder.encode(key, UTF_8) + "=" + URLEncoder.encode(value, UTF_8));
                    paramList.add(URLEncoder.encode("ctl00$MainContent$ctl01$pcSearchResults", UTF_8) + "=" + URLEncoder.encode("{&quot;activeTabIndex&quot;:2}", UTF_8));
                    paramList.add(URLEncoder.encode("ctl00$MainContent$ctl01$pcSearchResults$hiddenErrorCheck", UTF_8) + "=");
                    paramList.add(URLEncoder.encode("ctl00$MainContent$ctl01$pcSearchResults$grdWCJobList", UTF_8) + "=" + URLEncoder.encode("{&quot;keys&quot;:[],&quot;callbackState&quot;:&quot;OIxpDDEyWxXXjnhKwOoKQs6d7Er+T2TSN7Xt+2f128y/6Qu65v+abIQdWyyi525j5P0nGOSOI9vDXdEMEppUN6uVjCJHizeO73Kh5RXFD/vR+U54l38ChXF80FAAXOkpKAn/9FFXKigLb8vNWZatpNGjk6JrFu0hW4CVRl2ARWTerfx/jPpN8wCfWaG/AUQ7JivpmqBYQn7y3FKZLFYCXENMhbZPnkmOlxV3SbPLBHziWnW2vLUQj/i+agkE4AwToIUTZMAW6mckMbI5zWlbbqMYJHcGHmm+JJmCmvYU66GmK4mTaxWaTQMfmaFDmqkRqRg3WcAQLbh2I8yXSSxz+rudFg4PSJQXY/dy109QOiM1hX2GqREDNWmtO4vwqOs2GTf7IJGw42k+iqjdGxsAsv1O2xrs31V9bSIfWOEQYXM=&quot;,&quot;selection&quot;:&quot;&quot;}", UTF_8));
                    break;
                case "ctl00$MainContent$hidWorkCenter":
                    //paramList.add(
                    //  URLEncoder.encode("ctl00$MainContent$ctl02$grdWCFilter",UTF_8) + "="
                    //    + URLEncoder.encode("{&quot;lastMultiSelectIndex&quot;:-1,&quot;keys&quot;:[&quot;ASSEMBLY&quot;,&quot;BELTSAND&quot;,&quot;BRZ&quot;,&quot;BS&quot;,&quot;BV2&quot;,&quot;CAD&quot;,&quot;CIN2&quot;,&quot;CLEANBLACK&quot;,&quot;CNCLATHE&quot;,&quot;CNCMILL&quot;,&quot;CP3&quot;,&quot;CP4&quot;,&quot;CS&quot;,&quot;DAREX&quot;,&quot;DEBURR&quot;,&quot;DELIVERY&quot;,&quot;DEMAG&quot;,&quot;DKL&quot;,&quot;EDM-DRILL&quot;,&quot;EDM-WIRE&quot;,&quot;ENGR&quot;,&quot;EX-50V&quot;,&quot;HANDWORK&quot;,&quot;HONE&quot;,&quot;INSPECTION&quot;,&quot;K1&quot;,&quot;K2&quot;,&quot;LAPPING&quot;,&quot;MANGRIND&quot;,&quot;MANLATHE&quot;,&quot;MANMILL&quot;,&quot;MITCOMP&quot;,&quot;MNT&quot;,&quot;MRK&quot;,&quot;MYF&quot;,&quot;PG1000&quot;,&quot;PRESS&quot;,&quot;PW&quot;,&quot;PWR&quot;,&quot;PWR-LOADER&quot;,&quot;R-D&quot;,&quot;RCO&quot;,&quot;RECEIVING&quot;,&quot;RG6&quot;,&quot;S1&quot;,&quot;S33&quot;,&quot;SB&quot;,&quot;SHIPPING&quot;,&quot;STAMP&quot;,&quot;SUP&quot;,&quot;SWISH&quot;,&quot;TRAIN&quot;,&quot;TRU&quot;,&quot;TRUCK&quot;,&quot;V1&quot;,&quot;V2&quot;,&quot;WALT35&quot;,&quot;WALT44&quot;,&quot;WALT50&quot;,&quot;WIREWHL&quot;],&quot;callbackState&quot;:&quot;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&quot;,&quot;selection&quot;:&quot;&quot;}", UTF_8));
                    paramList.add(URLEncoder.encode(key, UTF_8) + "=" + URLEncoder.encode("{&quot;windowsState&quot;:&quot;0:0:-1:0:0:0:-10000:-10000:1:0:0:0&quot;}", UTF_8));
                    break;
                case "ctl00$TimoutControl1$TimeoutPopup$TPCFm1$TC$OkButton":
                    key = "ctl00$TimoutControl1$TimeoutPopupState";
                    paramList.add(URLEncoder.encode(key, UTF_8) + "=" + URLEncoder.encode("{&quot;windowsState&quot;:&quot;0:0:-1:0:0:0:-10000:-10000:1:0:0:0&quot;}", UTF_8));
                    break;
                default:
                    paramList.add(URLEncoder.encode(key, UTF_8) + "=" + URLEncoder.encode(value, UTF_8));
                    System.out.println("Key: " + key + "\nValue: " + value);
                    break;
            }
        }


        paramList.add(
                "DXScript"
                        + "="
                        + URLEncoder.encode(
                        "1_258,1_139,1_252,1_165,1_142,1_136,1_244,1_242,1_152,1_185,1_137", UTF_8));
        paramList.add(
                "DXCss"
                        + "="
                        + "0_1239%2C1_29%2C1_32%2C1_30%2C0_1241%2C1_9%2C1_10%2C1_8%2Chttp%3A%2F%2Fgc.kis.v2.scr.kaspersky-labs.com%2FE3E8934C-235A-4B0E-825A-35A08381A191%2Fabn%2Fmain.css%3Fattr%3DaHR0cDovLzEwLjEwLjguNC9kY21vYmlsZTIvKFMobmk0c2pjdmVwZWhkajBrMWF4c3kzd2cyKSkvRGVmYXVsdC5hc3B4%2C%2FDCMobile2%2FContent%2Fbootstrap.css%2C%2FDCMobile2%2FContent%2FSite.css%2Cfavicon.ico");

        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            System.out.println(param);
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&").append(param);
            }
        }
        return result.toString();
    }

    private String stopFormParams(String html, StopForm stopForm) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(html);

        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<>();

        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");
            System.out.println("Key: " + key);
            System.out.println("Value: " + value);

            switch (key) {
                case "ctl00$MainContent$pcStopFields":
                    value = "{&quot;activeTabIndex&quot;:0}";
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$hidRO0":
                    //Op Key
                    value = "";
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    //Add setup/run key value loaded via js
                    key = "ctl00$MainContent$pcStopFields$DC1";
                    value = stopForm.getSetupBool();
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$DC2":
                    //Labor Hours
                    value = String.valueOf(stopForm.getLaborHours());
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$DC3":
                    //Quantity Completed
                    value = String.valueOf(stopForm.getQuantityCompleted());
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$HidDC4":
                    //Rework check box
                    value = stopForm.getRework();
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    //If it is a rework add rework code
                    if (stopForm.getRework().equals("yes")) {
                        key = "ctl00$MainContent$pcStopFields$DC5";
                        value = String.valueOf(stopForm.getReworkCode());
                        paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    }
                    break;
                case "ctl00$MainContent$pcStopFields$DC6":
                    //Scrap Quantity
                    value = String.valueOf(stopForm.getScrapQuantity());
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    if (stopForm.getScrapQuantity() > 0) {
                        key = "ctl00$MainContent$pcStopFields$DC7";
                        value = String.valueOf(stopForm.getScrapCode());
                        paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    }
                    break;
                case "ctl00$MainContent$pcStopFields$DC8":
                    //Percent Complete
                    value = "0";
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$DC9":
                    //Notes
                    value = stopForm.getNotes();
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$pcStopFields$DC10":
                    //Op Complete
                    value = stopForm.getCompletedBool();
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$MainContent$btnOpStop":
                    value = "Submit";
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8));
                    break;
                case "ctl00$TimoutControl1$TimeoutPopupState":
                    value = "{&quot;windowsState&quot;:&quot;0:0:-1:0:0:0:-10000:-10000:1:0:0:0&quot;}";
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8).replace("$", "%"));
                    break;
                default:
                    key.replace("$", "%24");
                    paramList.add(key + "=" + URLEncoder.encode(value, UTF_8).replace("$", "%"));
                    break;
            }
        }
        paramList.add(
                "DXScript"
                        + "="
                        + URLEncoder.encode(
                        "1_258,1_139,1_252,1_165,1_142,1_136,1_244,1_242,1_152,1_185,1_137", UTF_8));
        paramList.add(
                "DXCss"
                        + "="
                        + "0_1239%2C1_29%2C1_32%2C1_30%2C0_1241%2C1_9%2C1_10%2C1_8%2Chttp%3A%2F%2Fgc.kis.v2.scr.kaspersky-labs.com%2FE3E8934C-235A-4B0E-825A-35A08381A191%2Fabn%2Fmain.css%3Fattr%3DaHR0cDovLzEwLjEwLjguNC9kY21vYmlsZTIvKFMobmk0c2pjdmVwZWhkajBrMWF4c3kzd2cyKSkvRGVmYXVsdC5hc3B4%2C%2FDCMobile2%2FContent%2Fbootstrap.css%2C%2FDCMobile2%2FContent%2FSite.css%2Cfavicon.ico");

        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&").append(param);
            }
        }
        return result.toString();
    }

    private void parseJobData(Job job, Elements elements) throws ParseException {
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
            //System.out.println(elements.get(i).text());
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

    private void parseRoutes(Job job, Elements elements) {
        Operation op = new Operation();
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
                    op.setDescription(elements.get(i).text());
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
        job.addOperation(op);
    }

    private void parsePicks(Job job, Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            //TODO: Catch number format exception
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
