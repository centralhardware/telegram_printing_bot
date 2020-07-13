package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

public class Config {

    private static final String telegramUsername    = System.getenv("TELEGRAM_USERNAME");
    private static final String telegramToken       = System.getenv("TELEGRAM_TOKEN");
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
    private static final Integer vkGroupId          = Integer.valueOf(System.getenv("VK_GROUP_ID"));
    private static final String vkGroupToken        = System.getenv("VK_GROUP_TOKEN");
    private static final boolean isEnableVk         = Boolean.parseBoolean(System.getenv("IS_ENABLE_VK"));
    private static final String pop3Host            = System.getenv("POP3_HOST");
    private static final String pop3User            = System.getenv("POP3_USER");
    private static final String pop3Password        = System.getenv("POP3_PASSWORD");
    private static final boolean isEmailEnabled     = Boolean.parseBoolean(System.getenv("IS_EMAIL_ENABLED"));
    private static final Long telegramGroupId       = Long.valueOf(System.getenv("TELEGRAM_GROUP_ID"));

    public static String getTelegramUsername() {
        return telegramUsername;
    }

    public static String getTelegramToken() {
        return telegramToken;
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

    public static Integer getVkGroupId() {
        return vkGroupId;
    }

    public static String getVkGroupToken() {
        return vkGroupToken;
    }

    public static boolean isIsEnableVk() {
        return isEnableVk;
    }

    public static String getPop3Host() {
        return pop3Host;
    }

    public static String getPop3User() {
        return pop3User;
    }

    public static String getPop3Password() {
        return pop3Password;
    }

    public static boolean getIsEmailEnabled() {
        return isEmailEnabled;
    }

    public static Long getTelegramGroupId() {
        return telegramGroupId;
    }
}
