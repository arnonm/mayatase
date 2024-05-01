package com.moneydance.modules.features.tlvbonds;

Live Demo
import java.util.*;
import java.text.*;
import java.lang.String;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MutualFundFile {
    
    private String p_FileName; 
    private static final String BaseURL = "https://info.tase.co.il/_layouts/Tase/Public/TargetFolder/"
    private String HeaderLine;
    // private array fundList = {}
    private boolean p_addOn;
    private static final String USER_AGENT = "Mozilla/5.0";
    private StringBuffer response;

    public MutualFundFile(Date date, boolean addOn) {

        p_addOn = addOn;
        String p_FileName = String.format("%s002%m%e01.tas", BaseURL, date, date);
        StringBuffer response = new StringBuffer();

        System.out.println(p_FileName);

    }

    public boolean getFile() {

        System.out.println("Getting Exchange data");
        URL obj = new URL(p_FileName);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
        //verify false
        //proxies
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            System.out.println("Data Recieved");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
            String inputLine;
            ParseFile(in);
            in.close();

            //self.ParsePage(page.content)
            return true;
        } else {
            System.out.println("Failed to Get Data");
            return false; 
        }

    }


    private void ParseFile(BufferedReader in) {
        String inputLine;
        int         linenum = 0;
        while ((inputLine = in.readLine()) != null) {
            linenum++;
            if (linenum == 1) {
                //print line
                StringBuffer HeaderLine = MutualFundsFileHeader(inputLine)
                # print(self.HeaderLine)
            } else {
                //print line
                StringBuffer CurrentFund = MutualFundLine(inputLine)
                //fundList.append(currentfund.MutualFundID]  = currentfund)
                //print(currentfund)
            }
                
            //response.append(inputLine);
        }
    }

    private StringBuffer MutualFundsFileHeader(String inputLine) {

    }

    private StringBuffer MutualFundLine(String inputLine) {

    }

}

