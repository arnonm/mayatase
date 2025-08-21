package com.maya.portfolio;

public class PortfolioLog {
    
    public static void error(Throwable t)
    {
        System.out.println(t.getMessage());
        System.out.println(t);
    }
}
