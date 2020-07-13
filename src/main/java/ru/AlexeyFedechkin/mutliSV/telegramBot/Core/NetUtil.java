package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {

    public static final Logger log = LoggerFactory.getLogger(NetUtil.class);

    /**
     * ping host
     * @param host domain to ping
     * @return true if reachable
     */
    public static boolean ping(String host){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(host).openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200){
                log.info(String.format("ping %s: host reachable", host));
                return true;
            }
        } catch (IOException e) {
            log.warn("failed to ping host " + host, e);
        }
        log.info(String.format("ping %s: host unreachable", host));
        return false;
    }

}
