package com.maya.requestclasses;



import com.maya.utils.Utils.Language;

public class MayaAllSecuritiesRequest extends MayaSecurityBaseRequest{
    
    private static final String END_POINT = "content/searchentities";
    private static final String METHOD = "GET";


    public  String getEndPoint() {
        return END_POINT;
    }
    
    public  String getMethod() {
        return METHOD;
    }

    public MayaAllSecuritiesRequest(Language lang) throws Exception{
        super();
        // System.out.println("MayaAllSecuritiesRequest::contructor");
        headers.put("Content-Type", "application/json");
        params.put("lang", Integer.toString(lang.getValue()));
        // System.out.println("in MayaAllSecurityRequest. aftersuper");

    }
}
