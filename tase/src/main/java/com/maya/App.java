package com.maya;

import java.util.Map;
import java.util.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.*;
import java.util.Optional;
import java.time.LocalDate;


import com.google.gson.Gson;
import com.maya.jsondata.SecurityListing;
import com.maya.utils.FullResponseBuilder;
import com.maya.utils.Utils.Language;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public final class App {

    protected HttpURLConnection con;
    protected Map<String, String> headers = new HashMap<>();
    protected Map<String, String> params = new HashMap<>();
    // protected Map<String, Object> response = new HashMap<>();
    Map<String, Object> response = new HashMap<>();
    // protected String response;
    int status = 0;
    
    private App() {
    }
    public static void main(String[] args) {

        System.out.println("Hello, World!");

        //Testing main = new Testing();
        //String ignore = main.TestRequest();
        // System.out.println(main.TestRequest());

        
        Logger logger = Logger.getLogger(App.class.getName());
        Maya maya = new Maya(logger, 1, false);
        
        List<SecurityListing> allSecurities; 

        try {
            
            /* 
            System.out.println("Testing::main - before getAllSecurities");
            allSecurities = maya.getAllSecurities(Language.ENGLISH);
            // System.out.println(allSecurities);

            System.out.println("Testing::main - after getAllSecurities");

            
            System.out.println("Testing:: getNames for Funds");
            System.out.println("Testing:: after GetNames - " +maya.getNames("5113428"));

            System.out.println("Testing:: getNames for Security");
            System.out.println("Testing:: after GetNames - " +maya.getNames("1135912"));

            System.out.println("Testing:: getDetails for Security");
            System.out.println(maya.getSecurityDetails("1135912",Language.ENGLISH));
            System.out.println("Testing:: getDetails for Funds");
            System.out.println(maya.getFundsDetails("5113428", Language.ENGLISH));
            
            */
            System.out.println("Testing:: getPriceHistory for Funds");
            System.out.println(maya.getPriceHistoryChunk("5113428",LocalDate.of(2024,10,30), LocalDate.of(2024,10,31), 1, Language.ENGLISH ));
            
            System.out.println("Testing:: getPriceHistory for Security");
            System.out.println(maya.getPriceHistoryChunk("1135912",LocalDate.of(2024,10,30), LocalDate.of(2024,10,31), 1, Language.ENGLISH ));
            
            // historical_prices = maya.getPriceHistory("1135912", LocalDate.of(2024, 10, 19), LocalDate.of(2024,10,19), 1, Language.ENGLISH);

            // for (fund_object in historical_prices) {
            //     System.out.println(fund_object);
            // }

            // System.out.println("Testing:: getPriceHistory for Fund");
            // historical_prices = maya.get_price_history(security_id="5118393", from_data=date(2017, 12, 31));
            // for (fund_object in historical_prices){
            //     System.out.println(fund_object);
            // }

         
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getClass());
            }
         /* 
         * // response = sendRequest();
         * Map <String, Object> allSecurities = new HashMap<>();
         * try {
         * System.out.println("before allSecurities mapping");
         * allSecurities = maya.getAllSecurities(Language.ENGLISH);
         * System.out.println("after allSecurities mapping");
         * System.out.println(maya.getNames("1135912"));
         * System.out.println(maya.getDetails("1135912", Language.ENGLISH));
         * 
         * // Map<String, Object> historicalPrices = maya.getPriceHistory("1135912",
         * Date("2024","10","10"), null, 1, Language.ENGLISH);
         * } catch (Exception e) {
         * System.out.println("Error");
         * }
         */
    }

    public void CookieManager() {
        CookieManager cookieManager = new CookieManager();
        String cookiesHeader = con.getHeaderField("Set-Cookie");
        Optional<HttpCookie> usernameCookie = null;
        if (cookiesHeader != null) {
            List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);
            cookies.forEach(cookie -> cookieManager.getCookieStore()
                    .add(null, cookie));
            usernameCookie = cookies.stream()
                    .findAny()
                    .filter(cookie -> cookie.getName()
                            .equals("username"));
        }

        if (usernameCookie == null) {
            cookieManager.getCookieStore()
                    .add(null, new HttpCookie("username", "john"));
        }

        con.disconnect();
    }

    public String TestRequest() {
        try {
            URL url = new URI("https://api.tase.co.il/api/content/searchentities?lang=0").toURL();
            this.con = (HttpURLConnection) url.openConnection();

            this.con.setRequestMethod("GET");

            // System.out.println("Setting params");
            // params.put("lang", "0");
            // this.con.setDoOutput(true);
            // DataOutputStream out = new DataOutputStream(con.getOutputStream());
            // out.writeBytes(getParamsString(params));
            // out.flush();
            // out.close();

            System.out.println("Testing::TestRequest - Setting headers");
            headers.put("Cache-Control", "no-cache");
            headers.put("referer", "https://www.tase.co.il/");
            headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; FSL 7.0.6.01001)");
            headers.put("Content-Type", "application/json");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

            this.con.setConnectTimeout(5000);
            this.con.setReadTimeout(5000);

            this.con.setInstanceFollowRedirects(false);

            System.out.println("Testing::TestRequest - RequestMethod");
            System.out.println(con.getRequestMethod());

            System.out.println("Testing::TestRequest - Headers");
            System.out.println(con.getRequestProperties());
            System.out.println("Testing::TestRequest - Params");
            System.out.println(con.getHeaderFields());

            System.out.println("Testing::TestRequest - Executing Request");
            status = this.con.getResponseCode();

            System.out.println("Testing::TestRequest - Status code: " + status);

            // System.out.println(FullResponseBuilder.getFullResponse(con));

            String responseString = getResponse(this.con);

            Gson gson = new Gson();
            Type SecurityListingType = new TypeToken <List<SecurityListing>>() {}.getType();
            List<SecurityListing> list = gson.fromJson(responseString, SecurityListingType);
            Iterator<SecurityListing> securityListingIterator = list.iterator();
            
            while (securityListingIterator.hasNext()) {
                SecurityListing listing = securityListingIterator.next();
                System.out.println("Testing::TestRequest - " +listing.Name + " "+listing.Type + " (" + listing.SubId + ")");
            }
            // System.out.println("Before list");
            // System.out.println(list);
            // System.out.println("After list");

            con.disconnect();
            return FullResponseBuilder.getFullResponse(con);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        return "";

        // return response;
    }

    // private Map<String, Object> getResponseDetails(HttpURLConnection con) {
    private StringBuffer getResponseDetails(HttpURLConnection con) {
        String inputLine;
        StringBuffer content = new StringBuffer();

        // Map<String, Object>content = new HashMap<>();
        try {
            // InputStream d = con.getInputStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                // content.put(inputLine, null);
                // System.out.println("input: "+inputLine);

            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return content;
    }

    public HttpURLConnection prepare() {

        this.con.setDoOutput(true);
        try {
            DataOutputStream out = new DataOutputStream(this.con.getOutputStream());
            out.writeBytes(getParamsString(params));
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.out.print(e.getStackTrace());
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.con.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return this.con;

    }

    public static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public String getResponse(HttpURLConnection con) throws IOException {
        Reader streamReader = null;

        if (con.getResponseCode() > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content.toString();
    }
}
