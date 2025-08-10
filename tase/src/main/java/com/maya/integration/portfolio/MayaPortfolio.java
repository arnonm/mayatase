package com.maya.integration.portfolio;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.maya.Maya;
import com.maya.utils.Utils.Language;

public class MayaPortfolio {
    
    private static Logger logger= Logger.getLogger(MayaPortfolio.class.getName());
    private static Maya maya = new Maya(logger, 1, false);
    

    

    public static Optional<LatestSecurityPrice> getLatestQuote(Security security) {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        LocalDate start;
        LatestSecurityPrice price = new LatestSecurityPrice();

       
        if (day == DayOfWeek.SUNDAY) {
        	start = LocalDate.now().minusDays(3);
        } else if (day == DayOfWeek.SATURDAY) {
        	start = LocalDate.now().minusDays(2);
        } else {
        	start = LocalDate.now().minusDays(1);
        }
    try {
            // Map <String, Object> history = maya.getPriceHistoryChunk(security.getTickerSymbol(), start, LocalDate.now(), 1, Language.ENGLISH );
            Map <String, Object> details = maya.getFundsDetails(security.getTickerSymbol(), Language.ENGLISH);   
            // Why not fund details

            Optional<String> time= Optional.ofNullable((String) details.get("RelevantDate"));
            if (time.isPresent()) {
                ZoneOffset exchangeZoneId = ZoneOffset.ofHours(-2);
                LocalDate localTime = LocalDate.parse(time.get().substring(0, 10));
                price.setDate(localTime);
            }

            Optional<String> quoteCurrency = Optional.of("ILS");
    

            Optional<Double> value = Optional.ofNullable((Double) details.get("UnitValuePrice"));
            if (value.isPresent()) {
                price.setValue(value.get());
            }

            // price.setValue(value.isPresent() ? Long.parseLong(value.get()) : LatestSecurityPrice.NOT_AVAILABLE);

            //Optional<String> value = extract value
            // if there is a value setCurrentCode

            // Optional<String> high = extract regular Market Day High
            price.setHigh(LatestSecurityPrice.NOT_AVAILABLE);

            // Optional <String> low = extract regular Market Day Low
            price.setLow(LatestSecurityPrice.NOT_AVAILABLE);

            // MarketVolume
            price.setVolume(LatestSecurityPrice.NOT_AVAILABLE);
            
            if (price.getDate() == null  || price.getValue() <= 0) {
                return Optional.empty();
            } else {
                return Optional.of(price);
            }
        } catch (Exception e) {
            logger.warning("Error " + e.getMessage() + ", traceback: " + e.getStackTrace());
            throw new RuntimeException(e);
        }   
        
    }
}
