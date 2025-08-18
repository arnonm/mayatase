package com.maya.integration.portfolio;

import java.io.Serializable;
import java.text.Collator;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class Security {
 

    private static class TextUtil{

        private final static Collator COLLATOR = Collator.getInstance();

        

        /* Performs a locale-sensitive comparison of two strings using Java Text
        * Collator.
        */
        public static int compare(String left, String right)
        {
            if (left == right)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            return COLLATOR.compare(left, right);
        }
    }

    public static final class ByName implements Comparator<Security>, Serializable
    {
        private static final long serialVersionUID = 1L;

        private final SecurityNameConfig config;

        public ByName()
        {
            this(SecurityNameConfig.NONE);
        }

        public ByName(SecurityNameConfig config)
        {
            this.config = config;
        }

        @Override
        public int compare(Security s1, Security s2)
        {
            if (s1 == null && s2 == null)
                return 0;
            else if (s1 == null)
                return -1;
            else if (s2 == null)
                return 1;

            return TextUtil.compare(s1.getName(config), s2.getName(config));
        }
    }

    private String uuid;
    private String onlineId;

    private String name;
    private String currencyCode = "EUR";
    private String targetCurrencyCode;

    private String note;

    private String isin;
    private String tickerSymbol;
    private String wkn;
    private String calendar;

    // feed and feedURL are used to update historical prices
    private String feed;
    private String feedURL;
    //private List<SecurityPrice> prices = new ArrayList<>();

    // latestFeed and latestFeedURL are used to update the latest (current)
    // quote. If null, the values from feed and feedURL are used instead.
    private String latestFeed;
    private String latestFeedURL;
    private LatestSecurityPrice latest;

    //private Attributes attributes;

    // private List<SecurityEvent> events;
    // private List<SecurityProperty> properties;
    private LocalDate date;
    private long value;


    private boolean isRetired = false;

    /**
     * Stores ephemeral data related to the current session that is not
     * persisted.
     */
    // private transient SecurityEphemeralData data; // NOSONAR

    private Instant updatedAt;


    public Security()
    {
        this.uuid = UUID.randomUUID().toString();
    }

    public Security(String name, String currencyCode)
    {
        this();
        this.name = name;
        this.currencyCode = currencyCode;
    }

    public Security(String name, String isin, String tickerSymbol, String feed)
    {
        this();
        this.name = name;
        this.isin = isin;
        this.tickerSymbol = tickerSymbol;
        this.feed = feed;
    }

    /* package */ Security(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUUID()
    {
        return uuid;
    }

    /* package */void generateUUID()
    {
        // needed to assign UUIDs when loading older versions from XML
        uuid = UUID.randomUUID().toString();
    }

    public void setTickerSymbol(String tickerSymbol)
    {
        this.tickerSymbol = tickerSymbol;
        this.updatedAt = Instant.now();
    }

     public String getName(SecurityNameConfig config)
    {
        switch (config)
        {
            case NONE:
                return name;
            case ISIN:
                return getIsin() != null ? getIsin() + " (" + name + ")" : name; //$NON-NLS-1$ //$NON-NLS-2$
            case TICKER_SYMBOL:
                return getTickerSymbol() != null ? getTickerSymbol() + " (" + name + ")" : name; //$NON-NLS-1$ //$NON-NLS-2$
            case WKN:
                return getWkn() != null ? getWkn() + " (" + name + ")" : name; //$NON-NLS-1$ //$NON-NLS-2$
            default:
                throw new IllegalArgumentException(config.name());
        }
    }

    public String getIsin()
    {
        return isin;
    }

     public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public String getWkn()
    {
        return wkn;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
        this.updatedAt = Instant.now();
    }
}
