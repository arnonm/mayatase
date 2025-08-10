package com.maya.integration.portfolio;

import java.time.LocalDate;
import java.util.Objects;

// import name.abuchen.portfolio.money.Values;

public class LatestSecurityPrice //extends SecurityPrice
{
    private long high;
    private long low;
    private long volume;
    private LocalDate date;
    private long value;

    @Deprecated
    /* package */ transient long previousClose;

    public static final long NOT_AVAILABLE = -1L;

    public LatestSecurityPrice()
    {
    }

    public LatestSecurityPrice(LocalDate date, long price)
    {
        this.value = price;
        this.date=date;
    }

    public LatestSecurityPrice(LocalDate date, long price, long high, long low, long volume)
    {
        this.value = price;
        this.date = date;

        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public long getHigh()
    {
        return high;
    }

    public void setHigh(long high)
    {
        this.high = high;
    }

    public long getLow()
    {
        return low;
    }

    public void setLow(long low)
    {
        this.low = low;
    }

    public long getVolume()
    {
        return volume;
    }

    public void setVolume(long volume)
    {
        this.volume = volume;
    }

    @Override
    public int hashCode()
    {
        return 31 * Objects.hash(date, value) + Objects.hash(high, low, volume);
    }

    private boolean internalequals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LatestSecurityPrice other = (LatestSecurityPrice) obj;
        return super.equals(other) && high == other.high && low == other.low && volume == other.volume;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!internalequals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LatestSecurityPrice other = (LatestSecurityPrice) obj;
        return internalequals(other) && high == other.high && low == other.low && volume == other.volume;
    }

    @Override
    @SuppressWarnings("nls")
    public String toString()
    {
        return String.format("%s %10s %10s %10s", super.toString(),
                        (high == NOT_AVAILABLE) ? "n/a" : String.format("%,10.2f", high / 1),
                        (low == NOT_AVAILABLE) ? "n/a" : String.format("%,10.2f", low / 1),
                        (volume == NOT_AVAILABLE) ? "n/a " : String.format("%,10d", volume));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
    public long getValue() {
        return value;
    }   
    public void setValue(long value)
    {
        this.value = value;
    }

    public void setValue(Double double1) {
        // TODO Auto-generated method stub
        this.value = double1.longValue();
    }
}
