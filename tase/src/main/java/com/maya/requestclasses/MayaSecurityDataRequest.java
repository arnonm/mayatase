package com.maya.requestclasses;

import com.maya.utils.Utils.Language;

public class MayaSecurityDataRequest  extends MayaSecurityBaseRequest{
    
    private static final String END_POINT = "company/securitydata";
    private static final String METHOD = "GET";

    public MayaSecurityDataRequest(String securityId, Language lang) throws Exception {
        super();
        this.params.put("securityId", securityId);
        this.params.put("lang", Integer.toString(lang.getValue()));
        this.headers.remove("Acccept");
        this.headers.remove("X-Maya-With");

    }

    @Override
    public String getEndPoint() {
        return END_POINT;
    }

    @Override
    public String getMethod() {
        return METHOD;
    }

    
}
