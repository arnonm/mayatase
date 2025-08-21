package com.maya.portfolio;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import  org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


import java.beans.Transient;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;
import java.time.DayOfWeek;
import java.time.LocalDate;


import com.google.gson.Gson;
import com.maya.portfolio.model.LatestSecurityPrice;
import com.maya.portfolio.PortfolioPerformance;
import com.maya.portfolio.online.QuoteFeedData;
import com.maya.portfolio.model.Security;
import com.maya.utils.GSONUtil;
import com.maya.utils.Utils.Language;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.maya.portfolio.model.LatestSecurityPrice;

/**
 * Unit test for simple App.
 */
public class TestPortfolio {
    



     /*****************************
      *  Portfolio Tests 
      *****************************/


    
     @Test
     void testGetLatestSecurityPriceforPortfolio() throws Exception {
        
        Security security = new Security();
        security.setCurrencyCode("ILS");
        security.setTickerSymbol("1135912");
        PortfolioPerformance portfolioPerformance = new PortfolioPerformance();
        
        Optional<LatestSecurityPrice> secPrice;
        LatestSecurityPrice price;
        
        try {

            secPrice = portfolioPerformance.getLatestQuote(security);
            price = secPrice.get();
            assertEquals(price.getDate(), LocalDate.of(2025, 8, 21));
            assertNotEquals(price.getHigh(), LatestSecurityPrice.NOT_AVAILABLE);
            assertNotEquals(price.getLow(), LatestSecurityPrice.NOT_AVAILABLE);
            assertEquals(price.getValue(), 11864);
            assertEquals(price.getVolume(), LatestSecurityPrice.NOT_AVAILABLE);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not be thrown");
        }
            

     }

     @Test
     void testGetLatestFundPriceforPortfolio() throws Exception {
        
        Security security = new Security();
        PortfolioPerformance portfolioPerformance = new PortfolioPerformance();

        security.setCurrencyCode("ILS");
        security.setTickerSymbol("5113428");
        
        Optional<LatestSecurityPrice> secPrice;
        LatestSecurityPrice price;
        
        try {

            secPrice = portfolioPerformance.getLatestQuote(security);
            price = secPrice.get();
            assertEquals(price.getDate(), LocalDate.of(2025, 8, 20));
            assertEquals(price.getHigh(), LatestSecurityPrice.NOT_AVAILABLE);
            assertEquals(price.getLow(), LatestSecurityPrice.NOT_AVAILABLE);
            assertEquals(price.getValue(), 14646);
            assertEquals(price.getVolume(), LatestSecurityPrice.NOT_AVAILABLE);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    void testEmptyFundPriceForPortfolio() throws Exception {
        
        Security security = new Security();
        PortfolioPerformance portfolioPerformance = new PortfolioPerformance();

        security.setCurrencyCode("ILS");
        security.setTickerSymbol("");
        
        Optional<LatestSecurityPrice> secPrice;
        
        try {
            secPrice = portfolioPerformance.getLatestQuote(security);
            assertFalse(secPrice.isPresent(), "Price should not be available for this fund");
        } catch (Exception e) {
            e.printStackTrace();
            // pass("Exception should not be thrown");
        }
    }

    @Test
    void testGetPriceHistorySecurity() throws Exception {
        
        Security security = new Security();
        PortfolioPerformance portfolioPerformance = new PortfolioPerformance();

        security.setCurrencyCode("ILS");
        security.setTickerSymbol("5113428");

        QuoteFeedData feedData;

        try {
            feedData = portfolioPerformance.getHistoricalQuotes(security, false);
            assertTrue(feedData.getErrors().isEmpty(), "Price history should be available");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not be thrown");
        }

    }

    @Test
    void testGetFundPriceHistory() throws Exception {   

    }

}
