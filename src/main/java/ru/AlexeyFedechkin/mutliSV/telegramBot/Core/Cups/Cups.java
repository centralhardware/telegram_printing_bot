package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import lombok.NonNull;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

@Component
@Scope(scopeName = "singleton")
public class Cups {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);
    private CupsPrinter cupsPrinter;

    @PostConstruct
    public void init(){
        try {
            CupsClient cupsClient = new CupsClient(Config.getCupsHost(), Config.getCupsPort());
            cupsPrinter = cupsClient.getDefaultPrinter();
            log.info("cups printer initialize");
            if (cupsPrinter == null){
                log.error("cups server don't have default printer");
            }
        } catch (Exception e) {
            log.error("failed to initialize printer", e);
        }
    }

    public boolean print(@NonNull File file, @NonNull String username, @NonNull String fileName) throws Exception {
        byte[] bytes = Files.readAllBytes(file.toPath());
        PrintJob printJob = new PrintJob.Builder(bytes).build();
        var printRequestResult = cupsPrinter.print(printJob);
        return printRequestResult.isSuccessfulResult();
    }

    public boolean checkAlive(){
        try {
            URL url = new URL(String.format("%s:%o", Config.getCupsHost(), Config.getCupsPort()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) return true;
        } catch (IOException ignored) { }
        return false;
    }

}
