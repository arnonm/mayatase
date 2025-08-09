package com.maya;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.Transient;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

import  org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.google.gson.Gson;
import com.maya.utils.GSONUtil;
import com.maya.utils.Utils.Language;

/**
 * Unit test for simple App.
 */
class AppTest {
    


    void testGetFundNamesStub() throws Exception{
    	// This test is not implemented yet
         Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        String response = "{\"english_short_name\":\"KSM KTF (0A) Tel Gov\",\"english_long_name\":\"KSM KTF (0A) Tel Gov\",\"hebrew_short_name\":\"ק.ש.מ ק\"}";
        String securityId = "123456";

        Map<String, String> names = Mockito.spy(new HashMap<>());
        Mockito.doReturn(response).when(maya).getNames(securityId);

        Map <String, String> realnames = maya.getNames(securityId);

        assertEquals("KSM KTF (0A) Tel Gov", names.get("english_short_name"));
        assertInstanceOf(String.class, names.get("english_long_name"));
        assertNotNull(names.get("english_long_name"), "String should not be null");
        assertFalse(names.get("english_long_name").isEmpty(), "String should not be empty");
    }

    @Test
    void testGetFundNamesLive() throws Exception{
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        System.out.println("Testing:: getNames for Funds");
        Map <String, String> names = maya.getNames("5113428");
        assertEquals("KSM KTF (0A) Tel Gov", names.get("english_short_name"));
        assertInstanceOf(String.class, names.get("english_long_name"));
        assertNotNull(names.get("english_long_name"), "String should not be null");
        assertFalse(names.get("english_long_name").isEmpty(), "String should not be empty");
        // System.out.println("Testing:: after GetNames - " + names);
    }

    
    void testGetSecurityDetailsHeb_Mock() throws Exception {
    	// This test is not implemented yet
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = Mockito.mock(Maya.class);

        String securityId = "1135912";
        Map<String, Object> mockDetails = new HashMap<>();
        mockDetails.put("HighRate", "118.55");
        mockDetails.put("Name", "ממשל צמודה 1025");

        Mockito.when(maya.getSecurityDetails(securityId, Language.HEBREW)).thenReturn(mockDetails);

        Map<String, Object> details = maya.getSecurityDetails(securityId, Language.HEBREW);

        assertFalse(details.isEmpty(), "Details should not be empty");
        assertNotNull(details.get("HighRate"), "HighRate should not be empty");
        assertInstanceOf(String.class, details.get("HighRate"));
        assertEquals("ממשל צמודה 1025", details.get("Name"));
    }

    @Test 
    void testGetSecurityNameLive() throws Exception {

        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        // System.out.println("Testing:: getNames for Security");
        
        Map <String, String> names = maya.getNames("1135912");
        //System.out.println("Testing:: after GetNames - " + names);  
        
        assertEquals("ILCPI % 1025", names.get("english_short_name"));
        assertInstanceOf(String.class, names.get("english_long_name"));
        assertEquals("ממשל צמודה 1025", names.get("hebrew_short_name"));
        assertInstanceOf(String.class, names.get("hebrew_long_name"));
        assertNotNull(names.get("english_long_name"), "String should not be null");
        assertTrue(names.get("english_long_name").isEmpty(), "String should not be empty");

    }

    @Test 
    void testGetSecurityDetailsEnglish() throws Exception {
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        String response = "{\"Id\":\"01135912\",\"Name\":\"ILCPI % 1025\",\"ISIN\":\"IL0011359127\",\"Type\":\" Government Bonds\",\"SubType\":0,\"BaseRate\":\"118.52\",\"HighRate\":\"118.55\",\"LowRate\":\"118.42\",\"OpenRate\":\"118.50\",\"InDay\":\"0\",\"ShareType\":\"0406\",\"EODTradeDate\":\"07/08/2025\",\"TurnOverValueShekel\":\"57322576.00\",\"MarketValue\":\"13101491\",\"CompanyName\":\"GALIL\",\"FullBranch\":\"\",\"CUSIP\":\"\",\"RegisteredCapital\":\"11055177366\",\"Exe\":\"\",\"ExeDesc\":\"\",\"ForeignMarket\":\"\",\"MinimumVolume\":\"8400\",\"MinimumVolumeBlock\":\"0\",\"DealsNo\":\"93\",\"OverallTurnOverUnits\":\"48372035\",\"MonthYield\":\"0.0\",\"AnnualYield\":\"2.9\",\"BrutoYield\":\"0.00\",\"RedemptionDate\":\"31/10/2025\",\"Linkage\":\"CPI\",\"AnnualInterest\":\"0.75000\",\"KeepStatusDate\":\"\",\"SuspendStatusDate\":\"\",\"IndexNumber\":\"\",\"IndexCategoryType\":\"\",\"UAssetName\":\"\",\"Symbol\":\"CPI1025\",\"PointsChange\":\"-0.01\",\"DaysUntilRedemption\":\"84\",\"BaseIndices\":\"99.6000000\",\"BaseIndicesDate\":\"15/06/2015\",\"CompanyLogo\":\"\",\"LastDealTime\":\"EoD\",\"IsForeignETF\":false,\"ExchangeDate\":\"\",\"LinkageType\":\"\",\"ExcessivePriceCurrency\":\"\",\"ExchangeShareName\":\"\",\"StrikeShareName\":\"\",\"ExchangeShareRate\":\"\",\"ExchangeRateType\":\"\",\"ExchangeRelation\":\"\",\"isTrading\":false,\"CompanyId\":\"954\",\"SecuritySubType\":\"Government bond - \\\"GALIL\\\"\",\"TradeDate\":\"07/08/2025\",\"TradeTime\":\"\",\"LastRate\":\"118.51\",\"Change\":\"-0.01\",\"GreenIndicators\":[],\"RedIndicators\":[],\"SecurityTypeInSite\":\"5\",\"ETFTypeInSite\":\"0\",\"SecurityLongName\":\"BANK OF ISRAEL - GALIL\",\"IsTASEUP\":false,\"AllowTasePlus\":true,\"HasOfferingPrice\":false,\"BlockDealTime\":\"EoD\"}";

        // System.out.println("Testing:: getDetails for Security");
        String securityId = "1135912";
        Map <String, Object> details = maya.getSecurityDetails(securityId, Language.ENGLISH);
        assertFalse(details.isEmpty(), "Details should not be empty");  
        assertNotNull(details.get("HighRate"), "HighRate should not be empty");  
        assertInstanceOf(String.class, details.get("HighRate"));  
        assertEquals("ILCPI % 1025", details.get("Name"));

    }

    @Test 
    void testGetSecurityDetailsHeb() throws Exception {
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        String response = "{\"Id\":\"01135912\",\"Name\":\"ILCPI % 1025\",\"ISIN\":\"IL0011359127\",\"Type\":\" Government Bonds\",\"SubType\":0,\"BaseRate\":\"118.52\",\"HighRate\":\"118.55\",\"LowRate\":\"118.42\",\"OpenRate\":\"118.50\",\"InDay\":\"0\",\"ShareType\":\"0406\",\"EODTradeDate\":\"07/08/2025\",\"TurnOverValueShekel\":\"57322576.00\",\"MarketValue\":\"13101491\",\"CompanyName\":\"GALIL\",\"FullBranch\":\"\",\"CUSIP\":\"\",\"RegisteredCapital\":\"11055177366\",\"Exe\":\"\",\"ExeDesc\":\"\",\"ForeignMarket\":\"\",\"MinimumVolume\":\"8400\",\"MinimumVolumeBlock\":\"0\",\"DealsNo\":\"93\",\"OverallTurnOverUnits\":\"48372035\",\"MonthYield\":\"0.0\",\"AnnualYield\":\"2.9\",\"BrutoYield\":\"0.00\",\"RedemptionDate\":\"31/10/2025\",\"Linkage\":\"CPI\",\"AnnualInterest\":\"0.75000\",\"KeepStatusDate\":\"\",\"SuspendStatusDate\":\"\",\"IndexNumber\":\"\",\"IndexCategoryType\":\"\",\"UAssetName\":\"\",\"Symbol\":\"CPI1025\",\"PointsChange\":\"-0.01\",\"DaysUntilRedemption\":\"84\",\"BaseIndices\":\"99.6000000\",\"BaseIndicesDate\":\"15/06/2015\",\"CompanyLogo\":\"\",\"LastDealTime\":\"EoD\",\"IsForeignETF\":false,\"ExchangeDate\":\"\",\"LinkageType\":\"\",\"ExcessivePriceCurrency\":\"\",\"ExchangeShareName\":\"\",\"StrikeShareName\":\"\",\"ExchangeShareRate\":\"\",\"ExchangeRateType\":\"\",\"ExchangeRelation\":\"\",\"isTrading\":false,\"CompanyId\":\"954\",\"SecuritySubType\":\"Government bond - \\\"GALIL\\\"\",\"TradeDate\":\"07/08/2025\",\"TradeTime\":\"\",\"LastRate\":\"118.51\",\"Change\":\"-0.01\",\"GreenIndicators\":[],\"RedIndicators\":[],\"SecurityTypeInSite\":\"5\",\"ETFTypeInSite\":\"0\",\"SecurityLongName\":\"BANK OF ISRAEL - GALIL\",\"IsTASEUP\":false,\"AllowTasePlus\":true,\"HasOfferingPrice\":false,\"BlockDealTime\":\"EoD\"}";

        // System.out.println("Testing:: getDetails for Security");
        String securityId = "1135912";
        Map <String, Object> details = maya.getSecurityDetails(securityId, Language.HEBREW);
        assertFalse(details.isEmpty(), "Details should not be empty");  
        assertNotNull(details.get("HighRate"), "HighRate should not be empty");  
        assertInstanceOf(String.class, details.get("HighRate"));  
        assertEquals("ממשל צמודה 1025", details.get("Name"));

    }
    @Test
    void testGetFundDetailsEnglish() throws Exception {
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        String response = "{english_short_name=KSM KTF (0A) Tel Gov, english_long_name=KSM KTF (0A) Tel Gov, hebrew_short_name=קסם KTF תל גוב- כללי, hebrew_long_name=קסם KTF תל גוב- כללי}";

        // System.out.println("Testing:: getDetails for Security");
        String fundId = "5113428";
        Map <String, Object> names = maya.getFundsDetails(fundId, Language.ENGLISH);
//        System.out.println(maya.getFundsDetails("5113428", Language.ENGLISH));

        assertFalse(names.isEmpty(), "Details should not be empty");  
        assertInstanceOf(String.class, names.get("FundLongName"));
        String fundLongName = names.get("FundLongName").toString();
        assertEquals("KSM KTF (0A) Tel Gov", fundLongName);
        assertNotNull(fundLongName, "String should not be null");
        assertFalse(fundLongName.isEmpty(), "String should not be empty");
    }

     @Test
     void testGetFundDetailsHebrew() throws Exception{
    	 Logger logger = Logger.getLogger(AppTest.class.getName());
         Maya maya = new Maya(logger, 1, false);
         
         String fundId = "5113428";
         Map <String, Object> names = maya.getFundsDetails(fundId, Language.HEBREW);
         System.out.println(maya.getFundsDetails("5113428", Language.HEBREW));
         
         assertFalse(names.isEmpty(), "Details should not be empty");  
         assertInstanceOf(String.class, names.get("FundLongName"));
         
         String fundLongName = names.get("ManagerLongName").toString();
         assertEquals("קסם קרנות נאמנות בע\"מ", fundLongName);
         assertNotNull(fundLongName, "String should not be null");
         assertFalse(fundLongName.isEmpty(), "String should not be empty");
     }

     @Test
     void testGetFundPriceHistory() throws Exception { 
        Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        
        //System.out.println("Testing:: getPriceHistory for Funds");
        String fundId = "5113428";
        
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        LocalDate start;
       
        if (day == DayOfWeek.FRIDAY) {
        	start = LocalDate.now().minusDays(3);
        } else if (day == DayOfWeek.SATURDAY) {
        	start = LocalDate.now().minusDays(2);
        } else {
        	start = LocalDate.now().minusDays(1);
        }
        
        Map <String, Object> history = maya.getPriceHistory(fundId, start, LocalDate.now(), 1, Language.ENGLISH );
        
        assertFalse(history.isEmpty(), "Details should not be empty");
        
        String table = history.get("Table").toString();
        assertTrue(table.length()!=0);
        
        
     }
        
     void testGetSecurityPriceHistory() throws Exception {
    	// This test is not implemented yet
    	Logger logger = Logger.getLogger(AppTest.class.getName());
        Maya maya = new Maya(logger, 1, false);
        System.out.println("Testing:: getPriceHistory for Security");
        System.out.println(maya.getPriceHistoryChunk("1135912",LocalDate.of(2024,10,30), LocalDate.of(2024,10,31), 1, Language.ENGLISH ));
     }
 

}
