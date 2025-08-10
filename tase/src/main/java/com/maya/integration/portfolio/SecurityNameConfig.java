package com.maya.integration.portfolio;


public enum SecurityNameConfig
{
    NONE("-"), //$NON-NLS-1$
    ISIN("ISIN"), //
    TICKER_SYMBOL("Ticker Symbol"), //
    WKN("WKN");

    private final String label;

    private SecurityNameConfig(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}
