package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;

import javax.mail.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class MailReceiver extends TimerTask {

    private static final long DElAY = 1000L;
    private static final Logger log = LoggerFactory.getLogger(MailReceiver.class);
    private String protocol = "imaps";
    private String file = "INBOX";
    private Session session;
    private Store store;
    private Folder folder;

    public static void initEmail() throws Exception {
        Timer timer = new Timer("emailReceiver");
        MailReceiver mailReceiver = new MailReceiver();
        mailReceiver.login(Config.getPop3Host(), Config.getPop3User(), Config.getPop3Password());
        timer.schedule(mailReceiver, 0, DElAY);
    }

    @Override
    public void run() {
        log.info("check mail");
        int messageCount = getMessageCount();

        //just for tutorial purpose
        if (messageCount > 5)
            messageCount = 5;
        try {
            Message[] messages = getMessages();
            for (int i = 0; i < messageCount; i++) {
                if (messages[i].getSubject() != null && messages[i].getSubject().equals("печать")){
                    Message message = messages[i];
                    System.out.println(message.getFileName());
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * to login to the mail host server
     */
    public void login(String host, String username, String password) throws Exception {
        URLName url = new URLName(protocol, host, 993, file, username, password);

        if (session == null) {
            Properties props;
            try {
                props = System.getProperties();
            } catch (SecurityException sex) {
                props = new Properties();
            }
            session = Session.getInstance(props, null);
        }
        store = session.getStore(url);
        store.connect();
        folder = store.getFolder(url);

        folder.open(Folder.READ_WRITE);
    }

    public int getMessageCount() {
        int messageCount = 0;
        try {
            messageCount = folder.getMessageCount();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return messageCount;
    }

    public Message[] getMessages() throws MessagingException {
        return folder.getMessages();
    }

}
