package com.qingting.model;

/**
 * Created by root on 2/1/16.
 */
public class Context {

    private String strName;
    private String strPassword;
    private String strUrl;
    private String strBase;
    private boolean bSecurity;
    private String strPublicKeyPath;
    private String strPrivateKeyPath;
    private String strOgnizationUnit;


    public Context(String strName, String strPassword, String strUrl, String strBase) {
        this.strName = strName;
        this.strPassword = strPassword;
        this.strUrl = strUrl;
        this.strBase = strBase;
        bSecurity = false;
        strPublicKeyPath = "";
        strPrivateKeyPath = "";
        strOgnizationUnit = "";
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getStrBase() {
        return strBase;
    }

    public void setStrBase(String strBase) {
        this.strBase = strBase;
    }

    public boolean isbSecurity() {
        return bSecurity;
    }

    public void setbSecurity(boolean bSecurity) {
        this.bSecurity = bSecurity;
    }

    public String getStrPublicKeyPath() {
        return strPublicKeyPath;
    }

    public void setStrPublicKeyPath(String strPublicKeyPath) {
        this.strPublicKeyPath = strPublicKeyPath;
    }

    public String getStrPrivateKeyPath() {
        return strPrivateKeyPath;
    }

    public void setStrPrivateKeyPath(String strPrivateKeyPath) {
        this.strPrivateKeyPath = strPrivateKeyPath;
    }

    public String getStrOgnizationUnit() {
        return strOgnizationUnit;
    }

    public void setStrOgnizationUnit(String strOgnizationUnit) {
        this.strOgnizationUnit = strOgnizationUnit;
    }
}
