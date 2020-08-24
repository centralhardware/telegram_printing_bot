package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class MailReceiver extends TimerTask {

    private static final long DElAY = 1000L;
    private static final Logger log = LoggerFactory.getLogger(MailReceiver.class);

    public static void initEmail() throws Exception {
        Timer timer = new Timer("emailReceiver");
        timer.schedule(new MailReceiver(), 0, DElAY);
    }

    @Override
    public void run() {
        log.info("check mail");
    }


}
