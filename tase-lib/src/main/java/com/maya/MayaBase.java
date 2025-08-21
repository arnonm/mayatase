package com.maya;

import java.util.logging.*;
import java.net.HttpURLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.maya.requestclasses.MayaBaseRequest;

// import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.client.methods.HttpUriRequest;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.util.EntityUtils;
// import org.python.google.common.cache.Cache;
// import java.net.CacheResponse;

import java.util.concurrent.TimeUnit;

public class MayaBase {

    protected Logger logger;
    private int numOfAttempts;
    private int status = 0;
    private String response;

    public MayaBase(Logger logger, int numOfAttempts, boolean verify) {

        this.logger = (logger == null) ? Logger.getLogger(this.getClass().getName()) : logger;
        this.numOfAttempts = numOfAttempts;
    }

    public String sendRequest(MayaBaseRequest mayaApiRequest) throws RuntimeException {
        logger.fine("MayaBase::sendRequest");
        try {
            this.response = getResponse(mayaApiRequest);
            if (this.status != 200) {

                logger.warning("Error in API call [" + this.status + "]");
                throw new IOException("status: " + status + " getURL: " + mayaApiRequest.getUrl() + " getMethod: "
                        + mayaApiRequest.getMethod());
            } else {
                logger.fine("MayaBase::sendRequest - got response. Status: " + this.status);

            }
            mayaApiRequest.disconnect();
            return this.response;

        } catch (Exception e) {
            logger.warning("Error " + e.getMessage() + ", traceback: " + e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    private String getResponse(MayaBaseRequest mayaApiRequest) throws Exception {

        logger.fine("MayaBase::getResponse -  Attempts " + numOfAttempts);
        for (int attempt = 0; attempt < numOfAttempts; attempt++) {
            logger.fine("MayaBase::getResponse - Preparing request");
            HttpURLConnection conn = mayaApiRequest.prepare();
            logger.fine("MayaBase::getResponse - Getting the response code");
            status = conn.getResponseCode();

            logger.fine("MayaBase::getResponse - After the response code");
            response = getResponseDetails(conn);
            logger.fine("MayaBase::getResponse - After getResponseDetails");

            if (status != 200) {
                logger.fine("MayaBase::getResponse - response error attempt: " + (attempt + 1) + "/" + numOfAttempts);
                logger.fine("MayaBase::getResponse - status response code " + status);
                logger.fine("MayaBase::getResponse - status response " + response);
                logger.fine("MayaBase::getResponse - try again in 1 sec...");
                TimeUnit.SECONDS.sleep(1);
            } else {
                logger.fine("Disconnecting");
                conn.disconnect();
                return response;
            }
            logger.fine("Disconnecting");
            conn.disconnect();
        }
        return response;
    }

    private String getResponseDetails(HttpURLConnection con) throws Exception {
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
