package com.maya.requestclasses;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.maya.utils.ParameterStringBuilder;

public abstract class MayaBaseRequest {
    private final static String REFERER_URL = "https://www.tase.co.il/";
    private final static String HOST = "api.tase.co.il";
    private final static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; FSL 7.0.6.01001)";

    protected Logger logger;
    protected Map<String, String> headers = new HashMap<>();
    protected Map<String, String> params = new HashMap<>();
    protected String URLParameters;
    protected String POSTParameters;
    public HttpURLConnection conn;

    public MayaBaseRequest(Object... args) throws IOException {
        // URL url = new URL(getUrl());
        // System.out.println("in MayaBaseRequest. URL "+url);
        // System.out.println("in MayaBaseRequest. opening connection");
        // request = (HttpURLConnection) url.openConnection();

        this.logger = (logger == null) ? Logger.getLogger(this.getClass().getName()) : logger;
        this.URLParameters = "";
        this.headers.put("Accept", "*/*"); // Not for fund
        this.headers.put("Cache-Control", "no-cache");
        this.headers.put("referer", REFERER_URL);
        this.headers.put("User-Agent", USER_AGENT);
        this.headers.put("Host", HOST);

        // System.out.println("Headers "+ headers);
    }

    protected abstract String getMayaApiBaseUrl();

    protected abstract String getEndPoint();

    public abstract String getMethod();

    public String getURLParameters() {
        return this.URLParameters;
    };

    public void disconnect() {
        this.conn.disconnect();
    }

    public String getUrl() {
        return getMayaApiBaseUrl() + getEndPoint() + getURLParameters();
    }


    public HttpURLConnection prepare() throws Exception {
        // System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        setExtendedURL();
        
        if (getMethod() == "POST") {
            this.POSTParameters = ParameterStringBuilder.getParamsString(this.params);
            this.headers.put("Content-Length", String.valueOf(this.POSTParameters.length()));
        }

        if (getMethod() == "POST" && getEndPoint() == "security/historyeod") {
            this.POSTParameters = ParameterStringBuilder.postJsonParamsString(this.params);
            this.headers.put("Content-Length", String.valueOf(this.POSTParameters.length()));
        }
        URL url = new URL(this.getUrl());
        logger.fine("MayaBaseRequest::prepare - URL " + this.getUrl());

        this.conn =  (HttpURLConnection)  url.openConnection();
        setHeaders();
        this.conn.setRequestMethod(getMethod());

        setPOSTParameters();
        // Syste m.out.println(getMethod() + " " + request.getURL());
        // System.out.println(request.getRequestProperties());
        // Show Headfers
        printRequestDetails();
        return this.conn;

    }

    private void setExtendedURL() throws Exception {
        if (this.getMethod() == "GET") {
            String tempParams = com.maya.utils.ParameterStringBuilder.getParamsString(this.params);
            if (tempParams.length() > 0) {
                this.URLParameters = "?" + tempParams;
            }
        }
    }

    public void setPOSTParameters() throws IOException {    
        if (this.getMethod() == "POST") {
            logger.fine("POST setParameters");
            this.conn.setDoOutput(true);
            // this.conn.setDoInput(true);
            try {
                DataOutputStream out = new DataOutputStream(this.conn.getOutputStream());
                logger.fine("Post parameters " + this.POSTParameters);
                out.write(this.POSTParameters.getBytes("UTF-8"));
                out.flush();
                out.close();


            } catch (IOException e) {
                logger.warning(e.getMessage());
                throw e;
            }
        }
    }

    public void setHeaders() {
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            logger.fine("MayaBaseRequest::setHeaders - set Request " +
                entry.getKey() + ": " + entry.getValue());
            this.conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        logger.fine("MayaBaseRequest::setHeaders - " +
           this.conn.getRequestProperties());
    }


    private void printRequestDetails() {
        logger.fine("MayaBaseRequest::printRequestDetails - URL " +
        this.getUrl());
        logger.fine("MayaBaseRequest::printRequestDetails - URL " +
        this.conn.getURL());
        logger.fine("MayaBaseRequest::printRequestDetails - RequestMethod " +
        this.conn.getRequestMethod());

        logger.fine("MayaBaseRequest::printRequestDetails - Headers:");
        try {
            logger.fine(this.conn.getRequestProperties().toString());
        } catch (Exception e) {
        }
        logger.fine("MayaBaseRequest::printRequestDetails - ContentType - " +
            this.conn.getContentType());
    }

}

/*
 * import java.net.URL;
 * import java.net.MalformedURLException;
 * import org.apache.http.client.methods.HttpRequestBase;
 * import org.apache.http.client.methods.HttpGet;
 * import org.apache.http.client.methods.HttpPost;
 * import org.apache.http.client.utils.URIBuilder;
 * 
 * public abstract class MayaBaseRequest {
 * protected HttpRequestBase request;
 * 
 * public MayaBaseRequest(Object... args) throws MalformedURLException {
 * this.conn = createRequest();
 * this.conn.addHeader("Cache-Control", "no-cache");
 * this.conn.addHeader("referer", "https://www.tase.co.il/");
 * this.conn.addHeader("User-Agent",
 * "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; FSL 7.0.6.01001)");
 * }
 * 
 * protected abstract String getMayaApiBaseUrl();
 * 
 * protected abstract String getEndPoint();
 * 
 * protected abstract String getMethod();
 * 
 * protected HttpRequestBase createRequest() {
 * if ("GET".equalsIgnoreCase(getMethod())) {
 * return new HttpGet();
 * } else if ("POST".equalsIgnoreCase(getMethod())) {
 * return new HttpPost();
 * }
 * throw new IllegalArgumentException("Unsupported HTTP method: " +
 * getMethod());
 * }
 * 
 * protected String getUrl() throws MalformedURLException {
 * return new URL(new URL(getMayaApiBaseUrl()), getEndPoint()).toString();
 * }
 * 
 * public HttpRequestBase prepare() throws Exception {
 * URIBuilder uriBuilder = new URIBuilder(getUrl());
 * request.setURI(uriBuilder.build());
 * 
 * System.out.println();
 * System.out.println(getMethod() + " " + request.getURI());
 * System.out.println(uriBuilder.getQueryParams());
 * // Note: In Java, you'd need to handle request body differently for POST
 * requests
 * System.out.println(request.getEntity());
 * System.out.println(request.getAllHeaders());
 * 
 * return request;
 * }
 * }
 * 
 * 
 */