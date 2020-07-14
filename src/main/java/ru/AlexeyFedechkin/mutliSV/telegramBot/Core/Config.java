package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import lombok.Getter;

import java.util.ResourceBundle;

public class Config {

    private static final ResourceBundle resource = ResourceBundle.getBundle("config");
    @Getter
    private static final String username    = resource.getString("username");
    @Getter
    private static final String token       = resource.getString("token");
    @Getter
    private static final String cupsHost    = resource.getString("cupsHost");
    @Getter
    private static final int    cupsPort    = Integer.parseInt(resource.getString("cupsPort"));
    @Getter
    private static final int    pagePrice   = Integer.parseInt(resource.getString("pagePrice"));
}
