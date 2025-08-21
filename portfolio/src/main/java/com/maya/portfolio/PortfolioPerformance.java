package com.maya.portfolio;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.maya.Maya;
import com.maya.portfolio.online.QuoteFeedException;
import com.maya.portfolio.model.LatestSecurityPrice;
import com.maya.portfolio.model.Security;
import com.maya.portfolio.model.SecurityPrice;
import com.maya.portfolio.online.QuoteFeed;
import com.maya.portfolio.utils.Dates;
import com.maya.portfolio.online.QuoteFeedData;
import com.maya.utils.Utils.Language;

public class PortfolioPerformance  implements QuoteFeed{
    
    private static Logger logger= Logger.getLogger(PortfolioPerformance.class.getName());
    private static Maya maya = new Maya(logger, 1, false);
    private static BigDecimal fmtPrice = new BigDecimal("1.0000");
    private static int factor = 10000;
    private static BigDecimal bdFactor = BigDecimal.valueOf(factor);

    public static final String ID = "TLV";

    static final ThreadLocal<DecimalFormat> FMT_PRICE = new ThreadLocal<DecimalFormat>()
    {
        @Override
        protected DecimalFormat initialValue()
        {
            DecimalFormat fmt = new DecimalFormat("0.###", new DecimalFormatSymbols(Locale.US)); //$NON-NLS-1$
            fmt.setParseBigDecimal(true);
            return fmt;
        }
    };

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public  String getName() {
        return "TLV Stocks";
    }

    public static long asPrice(String s) throws ParseException
    {
        return asPrice(s, BigDecimal.ONE);
    }
    
    static long asPrice(String s, BigDecimal factor) throws ParseException
    {
        if (s == null || s.isEmpty()) {
            return LatestSecurityPrice.NOT_AVAILABLE;
        }
        if ("N/A".equals(s) || "null".equals(s) || "NaN".equals(s)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return LatestSecurityPrice.NOT_AVAILABLE;

        BigDecimal v = (BigDecimal) FMT_PRICE.get().parse(s);
        
        BigDecimal vm = v.multiply(factor);
        BigDecimal vmbdFactor = vm.multiply(bdFactor);
        BigDecimal vmsetscale = vmbdFactor.setScale(0, RoundingMode.HALF_UP);
        long value = vmsetscale.longValue();
        return value;

        // return v.multiply(factor).multiply(bdFactor).setScale(0, RoundingMode.HALF_UP).longValue();
        
    }

    @Override
    public Optional<LatestSecurityPrice> getLatestQuote(Security security) {
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
            Map <String, Object> details = maya.getDetails(security.getTickerSymbol(), Language.ENGLISH);   
            System.out.println(details);

            Optional<String> time= Optional.ofNullable((String) details.get("RelevantDate"));
            if (time.isPresent()) {
                ZoneOffset exchangeZoneId = ZoneOffset.ofHours(-2);
                LocalDate localTime = LocalDate.parse(time.get().substring(0, 10));
                price.setDate(localTime);
            }

            //TradeDate
            Optional<String> tradeDate= Optional.ofNullable((String) details.get("TradeDate"));
            if (tradeDate.isPresent()) {
                ZoneOffset exchangeZoneId = ZoneOffset.ofHours(-2);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(tradeDate.get(), formatter);
                price.setDate(date);
            }

            // Hardcoded to ILA for Tel-Aviv Market
            Optional<String> quoteCurrency = Optional.of("ILA");
    

            Optional<String> value = Optional.ofNullable((String) details.get("UnitValuePrice"));
            if (value.isPresent()) {
                String p = value.get();
                System.out.println("UnitValuePrice: " + p);
                long asPrice = asPrice(p);
                System.out.println("UnitValuePrice asPrice: " + asPrice);
                System.out.println("Quote code: " + quoteCurrency.orElse(null));
                System.out.println("Security code: " + security.getCurrencyCode());
                System.out.println("Converted value: " + convertILS(asPrice, quoteCurrency.orElse(null), security.getCurrencyCode()));  
                price.setValue(convertILS(asPrice, quoteCurrency.orElse(null), security.getCurrencyCode()));
            }

            // LastRate
            Optional<String> lastRate = Optional.ofNullable((String) details.get("LastRate"));
            if (lastRate.isPresent()) {
                String p = lastRate.get();
                System.out.println("UnitValuePrice: " + p);
                long asPrice = asPrice(p);
                System.out.println("UnitValuePrice asPrice: " + asPrice);
                System.out.println("Quote code: " + quoteCurrency.orElse(null));
                System.out.println("Security code: " + security.getCurrencyCode());
                long convertedPrice = convertILS(asPrice, "ILA", "ILS");
                System.out.println("Converted value: " + convertedPrice);   
                System.out.println("Converted value: " + convertILS(asPrice, quoteCurrency.orElse(null), security.getCurrencyCode()));  
                price.setValue(convertILS(asPrice(lastRate.get()), quoteCurrency.orElse(null), security.getCurrencyCode()));
            }

            //Optional<String> value = extract value
            // if there is a value setCurrentCode

            // Optional<String> high = extract regular Market Day High - HighRate
            Optional<String> highRate = Optional.ofNullable((String) details.get("HighRate"));
            if (highRate.isPresent()) {
                price.setHigh(convertILS(asPrice(highRate.get()), quoteCurrency.orElse(null), security.getCurrencyCode()));
            }  else {
                price.setHigh(LatestSecurityPrice.NOT_AVAILABLE);
            }

            

            // Optional <String> low = extract regular Market Day Low - LowRate
            Optional<String> lowRate = Optional.ofNullable((String) details.get("LowRate"));
            if (lowRate.isPresent()) {
                price.setLow(convertILS(asPrice(lowRate.get()), quoteCurrency.orElse(null), security.getCurrencyCode()));
            }   else {
                    price.setLow(LatestSecurityPrice.NOT_AVAILABLE);
            }


            // MarketVolume
            price.setVolume(LatestSecurityPrice.NOT_AVAILABLE);

            if (price.getDate() == null  || price.getValue() <= 0) {
                return Optional.empty();
            } else {
                return Optional.of(price);
            }
        } catch (IOException | ParseException e) {
            return Optional.empty();

        } catch (Exception e) {
            logger.warning("Error " + e.getMessage() + ", traceback: " + e.getStackTrace());
            throw new RuntimeException(e);
        }   
        
    }

    @Override
    public QuoteFeedData getHistoricalQuotes(Security security, boolean collectRawResponse)
    {
        LocalDate start = calculateStart(security);
        return internalGetQuotes(security, start);

    }

    final LocalDate calculateStart(Security security) {
        if (security.getTickerSymbol() == null || security.getTickerSymbol().isEmpty()) {
            SecurityPrice lastHistoricalQuote = security.getPrices().get(security.getPrices().size() - 1);
            return lastHistoricalQuote.getDate();
        } else {
            return LocalDate.of(1900,1,1);
        }

    }

    private QuoteFeedData internalGetQuotes(Security security, LocalDate start) {
        
        if (security.getTickerSymbol() == null || security.getTickerSymbol().isEmpty()) {
            return QuoteFeedData.withError(
                new IOException(MessageFormat.format(Messages.MsgMissingTickerSymbol, security.getName())));
        }  
        
        try {

            String responseBody = requestData(security, start);

            return new QuoteFeedData();
        } catch (IOException e) {
            return QuoteFeedData.withError(e);
        }   
    }

    private String requestData(Security security, LocalDate startDate) throws IOException   {

        int days = Dates.daysBetween(startDate, LocalDate.now());
        try {
            Map <String, Object> history = maya.getPriceHistoryChunk(security.getTickerSymbol(), startDate, LocalDate.now(), 1, Language.ENGLISH );
        } catch (Exception e) {
            logger.warning("Error " + e.getMessage() + ", traceback: " + e.getStackTrace());
            throw new IOException();
        }
        return "";
    }


    QuoteFeedData ignore(Security security) {
        
         LocalDate quoteStartDate = null;

        if (!security.getPrices().isEmpty())
        {
            LocalDate lastPriceDate = security.getPrices().get(security.getPrices().size() - 1).getDate();

            // skip the download if
            // a) the configuration has not changed and we therefore can assume
            // historical prices have been provided by this feed *and*
            // b) there cannot be a newer price available on the server

            Optional<Instant> configChanged = security.getEphemeralData().getFeedConfigurationChanged();
            Optional<Instant> feedUpdate = security.getEphemeralData().getFeedLastUpdate();
            Boolean configHasNotChanged = configChanged.isPresent()
                            || (feedUpdate.isPresent() && feedUpdate.get().isAfter(configChanged.get()));

            if (configHasNotChanged)
            {
                ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
                LocalDate utcToday = utcNow.toLocalDate();

                // Check if symbol ends with ".TG" (Tradegate) and if it's after
                // 16:00 UTC
                boolean isTradegate = security.getTickerSymbol().endsWith(".TG"); //$NON-NLS-1$
                boolean after16UTC = utcNow.getHour() > 15;

                // For EU equities, it will be available only the next day.
                // For US equities, a couple hours after market closing at 22:00
                // UTC.
                LocalDate expectedAvailablePrice = (isTradegate && after16UTC) ? utcToday : utcToday.minusDays(1);

                // For the time being, use a minimal calendar (weekends,
                // christmas, new year). We can possibly switch to
                // exchange-specific trade calendar, however, let's start with
                // less aggressive caching. Do not use the trade calendar
                // configured by the user.
                // var tradeCalendar = TradeCalendarManager.getInstance(TradeCalendarManager.MINIMAL_CALENDAR_CODE);

                // while (tradeCalendar.isHoliday(expectedAvailablePrice))
                // {
                //     expectedAvailablePrice = expectedAvailablePrice.minusDays(1);
                // }

                if (lastPriceDate.equals(expectedAvailablePrice))
                {
                    // skip update b/c server cannot have newer data
                    return new QuoteFeedData();
                }
            }

            // adjust request to Monday to enable more aggressive caching
            quoteStartDate = lastPriceDate;
            if (quoteStartDate.getDayOfWeek() != DayOfWeek.MONDAY)
                quoteStartDate = quoteStartDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }
        else
        {
            // request 10 years starting Jan 1st to enable caching
            quoteStartDate = LocalDate.now().minusYears(10).withDayOfYear(1);
        }
        return getHistoricalQuotes(security, true);
    }

    @Override
    public QuoteFeedData previewHistoricalQuotes(Security security) throws QuoteFeedException
    {
        if (security.getTickerSymbol() == null)
        {
            return QuoteFeedData.withError(
                            new IOException(MessageFormat.format(Messages.MsgMissingTickerSymbol, security.getName())));
        }

        //return getHistoricalQuotes(security, true, LocalDate.now().minusMonths(2));
        return getHistoricalQuotes(security, true);
    }

    

    private static long convertILS(long price, String quoteCurrency, String securityCurrency)
    {   
        if (quoteCurrency != null)
        {
            if ("ILA".equals(quoteCurrency) && "ILS".equals(securityCurrency)) //$NON-NLS-1$ //$NON-NLS-2$
                return price / 100;
            if ("ILS".equals(quoteCurrency) && "ILA".equals(securityCurrency)) //$NON-NLS-1$ //$NON-NLS-2$
                return price * 100;
        }
        return price;
    }

    public long factorize(double value)
    {
        return Math.round(value * factor);
    }
}
