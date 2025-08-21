package com.maya;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// import com.google.gson.reflect.TypeToken;
import com.maya.utils.Utils.Language;
import com.maya.requestclasses.MayaFundDetailsRequest;
import com.maya.requestclasses.MayaFundHistoricalRequest;
import com.maya.jsondata.FundHistory;
import com.maya.jsondata.FundListing;
import com.maya.utils.GSONUtil;
import com.maya.adapters.FundsLocalDateTimeAdapter;

// import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
//import java.time.format.DateTimeFormatter;


public class MayaFunds extends MayaBase{
    
    public static final  int TYPE=4;
    private int period = 0;
    //private Type FundListingType = new TypeToken <FundListing>() {}.getType();
    

    public MayaFunds(Logger logger, int num_of_attempts, boolean verify) {
        super(logger, num_of_attempts, verify);
    }


    public  Map<String, String> getNames(
        FundListing englishDetails, FundListing hebrewDetails) 
    {
        Map<String, String>names = new HashMap<>();
        this.logger.fine("MayaFunds::getNames");
        names.put("english_short_name", (String) englishDetails.FundLongName);
        names.put("english_long_name", (String) englishDetails.FundLongName);
        names.put("hebrew_short_name", (String) hebrewDetails.FundShortName);
        names.put("hebrew_long_name", (String) hebrewDetails.FundShortName);
        return names;
    }

    
    public FundListing getDetails(String securityId, Language lang) throws Exception{
        this.logger.fine("MayaFunds::getDetails - before request");
        String responseString =  sendRequest(new MayaFundDetailsRequest(securityId, lang));
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new FundsLocalDateTimeAdapter())
            .create();

        FundListing listing  = gson.fromJson(responseString, FundListing.class);
        this.logger.fine("MayaFunds::getDetails - after request");
        return listing;
    }


    public FundHistory getPriceHistoryChunk(
        String securityId, LocalDate fromDate, LocalDate toDate, int page, Language lang) throws Exception {
            int _page = (page == 0) ? 1 : page ;
            String responseString = sendRequest(new MayaFundHistoricalRequest(securityId, fromDate, toDate, _page, period, lang));
            Gson gson = GSONUtil.createGson();
            FundHistory historyListing = gson.fromJson(responseString, FundHistory.class);
            //  return sendRequest(new MayaFundHistoricalRequest(securityId, fromDate, toDate, _page, period, lang));
            return historyListing;
            
    }

    //public Stream<Map<String, Object>> getPriceHistory (
    public FundHistory getPriceHistory (
        String securityId, LocalDate fromDate, LocalDate toDate, int page, Language lang) throws Exception 
    {
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        if (fromDate == null) {
            fromDate = toDate.minusDays(1);
        }
        // Map<String, Object> data = getPriceHistoryChunk(securityId, fromDate, toDate, _page, lang);
        return getPriceHistoryChunk(securityId, fromDate, toDate, page, lang);
        // Map<String, Object> items = (Map<String, Object>) data.getOrDefault("Items", new ArrayList<>());
        // return items.stream();
        // return data;
    }

}

