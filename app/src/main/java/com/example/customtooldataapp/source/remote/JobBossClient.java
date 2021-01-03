package com.example.customtooldataapp.source.remote;

import android.util.Log;
import android.util.Pair;

import com.example.customtooldataapp.models.Job;
import com.example.customtooldataapp.models.Operation;
import com.example.customtooldataapp.models.Transaction;
import com.example.customtooldataapp.source.ConnectionError;

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
import java.util.Arrays;
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

//TODO handle PICK AND BUY exceptions
//TODO grab Date data
public class JobBossClient {
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36";
    private final String employeeId;

    private static final String HOME = "/Home.aspx";
    private static final String DEFAULT = "/Default.aspx";
    private static final String JOB_ENTRIES = "/JobEntries.aspx";
    private static final String JOB_DETAILS = "/JobDetails.aspx";
    private List<HttpCookie> cookieList;
    private HttpURLConnection conn;
    private String urlBase = "http://10.10.8.4/dcmobile2/";
    private String viewSt = null;
    private String viewGen = null;
    private String eventVal = null;

    /**
     * Constructor, object requires employee id to function
     * @param employeeId
     */
    public JobBossClient(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Function that obtains all data for current employee transactions.
     * @return List of Transaction objects
     */
    public List<Transaction> getTransactions() throws ConnectionError {
/*        Transaction transaction1 = new Transaction();
        transaction1.setTranID("Transaction 1");
        transaction1.setLogout("No");
        Job job = new Job("Fake Job");
        Operation operation = new Operation("Fake Operation");
        transaction1.setJob(job);
        transaction1.setOperation(operation);

        Transaction transaction2 = new Transaction();
        transaction2.setTranID("Transaction 2");
        transaction2.setLogout("No");
        job = new Job("Fake Job");
        operation = new Operation("Fake Operation");
        transaction2.setJob(job);
        transaction2.setOperation(operation);

        Transaction transaction3 = new Transaction();
        transaction3.setTranID("Transaction 3");
        transaction3.setLogout("Yes");
        job = new Job("Fake Job");
        operation = new Operation("Fake Operation");
        transaction3.setJob(job);
        transaction3.setOperation(operation);

        return new ArrayList<>(Arrays.asList(transaction1, transaction2, transaction3));*/

        //Initialize ArrayList
        List<Transaction> transactions = new ArrayList<>();

        //Begin connecting
        initializeConnection();

        //Login to website
        login();

        //Obtain response body from the Job Entries page
        Document document = Jsoup.parse(getPageContent(JOB_ENTRIES));



        //Break html up by transactions
        Elements links = document.getElementsByAttribute("href");

        //For each transaction
        for (Element elem : links) {
            // Move through all the links and find the entry routes
            String link = elem.attr("href");
            if (link.contains("OpStop.aspx")) {
                //Create Transaction object
                Transaction singleTransaction = new Transaction(link);

                //Get Job Data
                Pair<Job, Operation> pair = getJobData(JOB_DETAILS.concat("?id=" + elem.text()), singleTransaction.getOperationId());

                // Set Job data
                singleTransaction.setJob(pair.first);
                singleTransaction.setOperation(pair.second);

                //Add Transaction to list
                transactions.add(singleTransaction);
            }
        }
        return transactions;
    }

    /**
     * Establishes initial connection to set the session id and cookies
     */
    private void initializeConnection() throws ConnectionError {
        //Get HTML page content
        Document document = Jsoup.parse(getPageContent(DEFAULT));

        //Find <input> HTML elements
        Elements inputElements = document.getElementsByTag("input");

        //Iterates over input elements and assigns the values to global variables.
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
    }

    /**
     * Logs the employee into the website
     * @throws IOException If there is a IOConnection pertaining to the Server such as timeout
     */
    private void login() throws ConnectionError {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("__LASTFOCUS", "")
                .addFormDataPart("__EVENTTARGET", "")
                .addFormDataPart("__EVENTARGUMENT", "")
                .addFormDataPart("__VIEWSTATE", viewSt)
                .addFormDataPart("__VIEWSTATEGENERATOR", viewGen)
                .addFormDataPart("__EVENTVALIDATION", eventVal)
                .addFormDataPart("ctl00$MainContent$txtLogin", employeeId)
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

        try{
            client.newCall(request).execute();
        }catch(IOException e){
            throw new ConnectionError("Login Error");
        }

    }

    /**
     * Returns a String containing the page html.
     */
    private String getPageContent(String url_ext) throws ConnectionError {
        //Initialize cookie manager and cookie store
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        CookieStore cookieStore = cookieManager.getCookieStore();


        try {
            //Create url
            URL obj = new URL(urlBase.concat(url_ext));
            //Open connection
            conn = (HttpURLConnection) obj.openConnection();

            //Set connection properties
            conn.setInstanceFollowRedirects(true);
            conn.setUseCaches(true);
            conn.setConnectTimeout(5000);
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

            //If cookie list is NOT empty add Cookie property
            if (!(cookieList == null)) {
                for (HttpCookie cookie : cookieList) {
                    conn.setRequestProperty("Cookie", cookie.toString());
                }
            }

            Log.d("getPageContent()", "\n\tSending 'GET' request to URL: " + urlBase.concat(url_ext));
            Log.d("getPageContent()", "\n\tResponse Code: " + conn.getResponseCode());

            //Initialize BufferedReader
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            Log.d("getPageContent()", "Reading input...");

            //While there is more lines read input
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            //Close reader
            in.close();

            //Sets session id if not already done
            setSessionId(conn.getURL().toString());

            // Get the response cookies if there is any
            cookieList = cookieStore.getCookies();

            //Return response
            return response.toString();

        }catch (IOException e){
            throw new ConnectionError(e.getMessage() + "\nGET Request Error at: " + urlBase.concat(url_ext));
        }
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

    /**
     * Obtains Job and Operation Data for the Transaction
     * @param operationId
     * @return Pair<Job, Operation> Job Data with Transaction's current Route
     */
    private Pair<Job, Operation> getJobData(String url, String operationId) throws ConnectionError {
        String html;
        try {
            html = getPageContent(url);
        }catch(IOException e){
            //If there is a IOException return Objects with the appropriate error code
            throw new ConnectionError(e.toString());
        }

        //Initializes Job and Operation objects
        Job job = new Job(employeeId);
        Operation operation = new Operation(operationId);

        //Parse HTML response
        Document doc = Jsoup.parse(html);

        //Grab Job Data block from page
        Elements jobData = doc.getElementById("JobData").getElementsByTag("span");

        //Grab Quantity Data block from page
        Elements qtyData = doc.getElementById("QtyData").getElementsByTag("span");

        //Grab Customer Data block from page
        Elements customerData = doc.getElementById("CustData").getElementsByTag("span");

        //Grab Route Data block from page
        Elements routes =
                doc.getElementById("grdRouting_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdRouting_DXDataRow."));

        //Grab Pick Data block from page
        Elements picks =
                doc.getElementById("grdPick_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdPick_DXDataRow."));

        //Grab Buy Data block from page
        Elements buys =
                doc.getElementById("grdBuy_DXMainTable")
                        .getElementsByAttributeValueMatching("id", Pattern.compile("^grdBuy_DXDataRow."));

        //Parse and Assign Job Data
        parseJobData(job, jobData);

        //Parse and Assign Quantity Data
        parseQtyData(job, qtyData);

        //Parse and Assign Customer Data
        parseCustomerData(job, customerData);

        //If Route Sata exists Parse and Assign Route data for Transaction's Route only.
        if (routes.size() > 0) {
            for (Element elem : routes) {
                //Check for the Transaction's operation
                if (elem.getElementsByTag("td").get(0).text().equals(operationId)) {
                    //Parse and Assign Route data
                    parseRoutes(elem.getElementsByTag("td"), operation);
                }
            }
        }

        //If Pick Data exists Parse and Assign Pick Data
        if (picks.size() > 0) {
            parsePicks(job, picks.get(0).getElementsByTag("td"));
        }

        //If Buy Data exists Parse and Assign Pick Data
        if (buys.size() > 0) {
            parseBuys(job, buys.get(0).getElementsByTag("td"));
        }

        //Return the Job object paired with the Transactions current Route
        return new Pair<>(job, operation);
    }

    /**
     * Parses Job data from web page
     * @param job
     * @param elements
     */
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

    /**
     * Parses Quantity data from webpage
     * @param job
     * @param elements
     */
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

    /**
     * Parses Customer data from web page
     * @param job
     * @param elements
     */
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

    /**
     * Parses Route data from webpage
     * @param elements
     * @param op
     * @return
     */
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

    /**
     * Parses Pick data from web page
     * @param job
     * @param elements
     */
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

    /**
     * Parses buy data from web page
     * @param job
     * @param elements
     */
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

