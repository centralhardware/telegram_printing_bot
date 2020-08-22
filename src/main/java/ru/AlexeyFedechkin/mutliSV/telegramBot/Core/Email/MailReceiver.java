package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class MailReceiver extends TimerTask {

    private static final long DElAY = 1000L;
    private static final Logger log = LoggerFactory.getLogger(MailReceiver.class);

    public static void initEmail(){
        Timer timer = new Timer("emailReceiver");
        timer.schedule(new MailReceiver(), 0, DElAY);
    }

    private Properties properties;
    private final Authenticator auth = new EmailAuthenticator(Config.getPop3User(), Config.getPop3Password());
    private Session session;

    private void configure(){
        if (properties == null){
            properties = new Properties();
            properties.put("mail.debug"          , "true"  );
            properties.put("mail.store.protocol" , "imaps"  );
            properties.put("mail.imap.ssl.enable", "true"   );
            properties.put("mail.imap.port"      , 993);
        }
        if (session == null){
            session = Session.getDefaultInstance(properties, auth);
            session.setDebug(false);
        }
    }

    @Override
    public void run() {
        configure();

        log.info("check mail");
        try {
            Store store = session.getStore();

            // Подключение к почтовому серверу
            store.connect(Config.getPop3Host(), Config.getPop3User(), Config.getPop3Password());

            // Папка входящих сообщений
            Folder inbox = store.getFolder("INBOX");

            // Открываем папку в режиме только для чтения
            inbox.open(Folder.READ_WRITE);

            if (inbox.getMessageCount() == 0) return;
            Part[] messages = inbox.getMessages();
            for (Part part : messages){
                if (part instanceof Message){
                    if (part.isMimeType("multipart/*")){
                        Multipart multipart = (Multipart) part.getContent();
                        for (int i = 0; i < multipart.getCount(); i++) {
                            BodyPart bodyPart = multipart.getBodyPart(i);
                            if (bodyPart.getContentType().equals("pdf/")){

                            }
                        }
                    }
                }
            }
        } catch (MessagingException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
