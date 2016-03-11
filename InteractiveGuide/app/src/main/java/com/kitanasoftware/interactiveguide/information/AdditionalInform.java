package com.kitanasoftware.interactiveguide.information;

import java.io.Serializable;

/**
 * Created by dasha on 29/01/16.
 */
public class AdditionalInform extends Information implements Serializable{
    private String companyName;
    private static final long serialVersionUID = 1996156934189183983L;
    public AdditionalInform(InformType type, String companyName) {
        super(type);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
