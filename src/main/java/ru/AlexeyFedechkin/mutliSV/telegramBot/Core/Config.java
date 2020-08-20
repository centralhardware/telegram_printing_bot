package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import java.util.ResourceBundle;

public class Config {

    private static final String username            = System.getenv("USERNAME");
    private static final String token               = System.getenv("TOKEN");
    private static final String cupsHost            = System.getenv("CUPS_HOST");
    private static final int    cupsPort            = Integer.parseInt(System.getenv("CUPS_PORT"));
    private static final int    pagePrice           = Integer.parseInt(System.getenv("PAGE_PRICE"));
    private static final String sberbankUserName    = System.getenv("SBERBANK_USERNAME");
    private static final String sberbankPassword    = System.getenv("SBERBANK_PASSWORD");
    private static final String baseUrl             = System.getenv("BASE_URL");
    private static final String sberbankFailUrl     = baseUrl + "/fail";
    private static final String sberbankSuccessUrl  = baseUrl + "/success";
    private static final boolean isEnableQr         = Boolean.parseBoolean(System.getenv("IS_ENBALE_QR"));
    private static final String companyName         = System.getenv("COMPANY_NAME");
    private static final String companyLocation     = System.getenv("COMPANY_LOCATION");
    private static final String embededMapIfram     = System.getenv("EMBEDDED_MAP");
    private static final String workingTime         = System.getenv("COMPANY_WORKING_TIME");

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

    public static String getWorkingTime() {
        return workingTime;
    }
}
