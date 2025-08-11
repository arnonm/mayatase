package com.maya;


import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.maya.utils.Utils.Language;
import com.maya.jsondata.SecurityListing;
import com.maya.jsondata.FundHistory;
import com.maya.jsondata.FundListing;
import com.maya.jsondata.SecurityHistory;
import com.maya.utils.GSONUtil;

public class Maya {

    private MayaSecurity mayaSecurities;
    private MayaFunds mayaFunds;
    private Map<String, Set<String>> mappedSecurities;
    private Type securityListingType;
    private Logger logger;



    public Maya(Logger logger, int number_of_attempts, boolean verify) {

        this.mayaSecurities = new MayaSecurity(logger, number_of_attempts, verify);
        this.mayaFunds = new MayaFunds(logger, number_of_attempts, verify);
        this.mappedSecurities = new HashMap<>();
        this.securityListingType = new TypeToken <List<SecurityListing>>(){}.getType();
        this.logger = (logger == null) ? Logger.getLogger(this.getClass().getName()) : logger;


        try {
            this.mapSecurities();
        } catch (Exception e) {
            logger.severe("");
        }
    }

    public Maya() {
        this(null, 1, true);
    }

    public List<SecurityListing> getAllSecurities(Language lang) throws Exception {
        logger.fine("Maya::getAllSecurities");
        return mayaSecurities.getAllSecurities(lang);

    }

    private void mapSecurities() throws Exception {
        List<SecurityListing> allSecuritiesList = getAllSecurities(Language.ENGLISH);
        Iterator<SecurityListing> securityListingIterator = allSecuritiesList.iterator();

        while (securityListingIterator.hasNext()) {
            SecurityListing listing = securityListingIterator.next();
            String id = listing.Id;
            String type =listing.Type;
            mappedSecurities.computeIfAbsent(id, k-> new HashSet<>()).add(type);
        }
    }

    private Object getMayaClass(String securityId) {
        logger.fine("Maya::getMayaClass - " + mappedSecurities.get(securityId));
        if (mappedSecurities.get(securityId).contains(Integer.toString(MayaFunds.TYPE))) {
            logger.fine("Maya::getMayaClass - Type is MayaFunds");
            return mayaFunds;
        } else {
            logger.fine("Maya::getMayaClass - Type is MayaSecurity");
            return mayaSecurities;
        }
    }

    /* was single functon called getDetails for both getDetails. Will refactor as one after finishing funds
    public String getDetails(String securityId, Language lang) throws Exception {
        Object mayaClass = getMayaClass(securityId);
        if (mayaClass instanceof MayaFunds) {
            return ((MayaFunds) mayaClass).getDetails(securityId, lang);
        } else {
            return ((MayaSecurity) mayaClass).getDetails(securityId, lang);
        }
    }
    */
    public Map<String, Object> getSecurityDetails(String securityId, Language lang) throws Exception {
        Object mayaClass = getMayaClass(securityId);
        SecurityListing listing =  ((MayaSecurity) mayaClass).getDetails(securityId, lang);
        return this.SecurityToMap(listing);
    }


    private Map<String, Object> SecurityToMap(SecurityListing listing) {
        //Gson gson = new Gson();
        Gson gson = GSONUtil.createGson();
        String json = gson.toJson(listing);
        logger.fine("JSON "+json);
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return map;
    }

    private Map<String, Object> FundsToMap(FundListing listing) {
        Gson gson = GSONUtil.createGson();
        //Gson gson = new Gson();
         String json = gson.toJson(listing);
        logger.fine("JSON "+json);
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return map;
    }

    private Map<String, Object> HistoryToMap(Object listing) {
        Gson gson = GSONUtil.createGson();
        String json = gson.toJson(listing);
        logger.fine("JSON "+json);
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return map;

    }
    public Map<String, Object> getFundsDetails(String securityId, Language lang) throws Exception {
        Object mayaClass = getMayaClass(securityId);
        FundListing listing = ((MayaFunds) mayaClass).getDetails(securityId, lang);
        return this.FundsToMap(listing);
    }

    public Map<String, String> getNames(String securityId) throws Exception {
        logger.fine("Maya::getNames");
        Object mayaClass = getMayaClass(securityId);
        if (mayaClass instanceof MayaFunds) {
            FundListing englishDetails = ((MayaFunds) mayaClass).getDetails(securityId, Language.ENGLISH);
            FundListing hebrewDetails = ((MayaFunds) mayaClass).getDetails(securityId, Language.HEBREW);
                return ((MayaFunds) mayaClass).getNames(englishDetails, hebrewDetails);
        } else {
            SecurityListing englishDetails = ((MayaSecurity) mayaClass).getDetails(securityId, Language.ENGLISH);
            SecurityListing hebrewDetails = ((MayaSecurity) mayaClass).getDetails(securityId, Language.HEBREW);
            return ((MayaSecurity) mayaClass).getNames(englishDetails, hebrewDetails);
        }
    }

    public Map<String, Object> getPriceHistoryChunk(String securityId, LocalDate fromDate, LocalDate toDate, int page, Language lang) throws Exception {
        logger.fine("Maya::getPriceHistoryChunk");
        Object mayaClass = getMayaClass(securityId);
        if (mayaClass instanceof MayaFunds) {
            FundHistory fundHistory =  ((MayaFunds) mayaClass).getPriceHistoryChunk(securityId, fromDate, toDate, page, lang);
            return this.HistoryToMap(fundHistory);
        }  else {
            SecurityHistory securityHistory = ((MayaSecurity) mayaClass).getPriceHistoryChunk(securityId, fromDate, toDate, page, lang);
            return this.HistoryToMap(securityHistory);
        }
    }

    public Map<String, Object> getPriceHistory(String securityId, LocalDate fromDate,
            LocalDate toDate, int page, Language lang) throws Exception {
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        if (fromDate == null) {
            fromDate = toDate.minusDays(1);
        }
        Object mayaClass = getMayaClass(securityId);
        if (mayaClass instanceof MayaFunds) {
            FundHistory fundHistory = ((MayaFunds) mayaClass).getPriceHistory(securityId, fromDate, toDate, page, lang);
            return HistoryToMap(fundHistory);
        } else {
            SecurityHistory securityHistory = ((MayaSecurity) mayaClass).getPriceHistory(securityId, fromDate, toDate, page, lang);
            return HistoryToMap(securityHistory);
        }
    }

    public float getSellPrice(String securityId, LocalDate fromDate, LocalDate toDate, Language lang) throws Exception {
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        if (fromDate == null) {
            fromDate = toDate.minusDays(1);
        
        }
        Map <String, Object> history = getPriceHistoryChunk(securityId, fromDate, toDate, 1, lang );
        
        if (history.isEmpty()) {
            return 0.0f;
        }
        
        Object obj = history.get("Table");
        if (obj instanceof ArrayList) {
            ArrayList<?> list = (ArrayList<?>) history.get("Table");
            if (list.isEmpty()) {
                return 0.0f;
            }
            Object first = list.get(0);
            if (first instanceof Map) {
                Object sellPrice = ((Map<?, ?>) first).get("SellPrice");
                return sellPrice instanceof Number ? ((Number) sellPrice).floatValue() : 0.0f;
            } else {
                return 0.0f; // or handle the case where the first element is not a Map
            }
            
        } else {
            return 0.0f; // or handle the case where the object is not an ArrayList
        }
    }

}
