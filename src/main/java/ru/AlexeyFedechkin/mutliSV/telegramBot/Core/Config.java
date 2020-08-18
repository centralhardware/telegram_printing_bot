package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import java.util.ResourceBundle;

public class Config {

    private static final ResourceBundle resource    = ResourceBundle.getBundle("application");
    private static final String username            = resource.getString("username");
    private static final String token               = resource.getString("token");
    private static final String cupsHost            = resource.getString("cupsHost");
    private static final int    cupsPort            = Integer.parseInt(resource.getString("cupsPort"));
    private static final int    pagePrice           = Integer.parseInt(resource.getString("pagePrice"));
    private static final String sberbankUserName    = resource.getString("sberbankUserName");
    private static final String sberbankPassword    = resource.getString("sberbankPassword");
    private static final String baseUrl             = resource.getString("baseUrl");
    private static final String sberbankFailUrl     = baseUrl + "/payment/fail";
    private static final String sberbankSuccessUrl  = baseUrl + "/payment/success";
    private static final boolean isEnableQr         = Boolean.parseBoolean(resource.getString("isEnableQr"));
    private static final String companyName         = resource.getString("companyName");
    private static final String companyLocation     = resource.getString("companyLocation");
    private static final String embededMapIfram     = resource.getString("embeddedMap");

    public static ResourceBundle getResource() {
        return resource;
    }

    public static String getUsername() {
        return username;
    }

    public static String getToken() {
        return token;
    }

    public static String getCupsHost() {
        return cupsHost;
    }

    public static int getCupsPort() {
        return cupsPort;
    }

    public static int getPagePrice() {
        return pagePrice;
    }

    public static String getSberbankUserName() {
        return sberbankUserName;
    }

    public static String getSberbankPassword() {
        return sberbankPassword;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getSberbankFailUrl() {
        return sberbankFailUrl;
    }

    public static String getSberbankSuccessUrl() {
        return sberbankSuccessUrl;
    }

    public static boolean isIsEnableQr() {
        return isEnableQr;
    }

    public static String getCompanyName() {
        return companyName;
    }

    public static String getCompanyLocation() {
        return companyLocation;
    }

    public static String getEmbeddedMapIframe() {
        return embededMapIfram;
    }
}
