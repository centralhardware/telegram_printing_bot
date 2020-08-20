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

    /**
     * create instance of cupsPrinter
     */
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

    /**
     * print document via cups server
     * @param file file to print
     * @param username user that send document to print
     * @param fileName original file name
     * @return
     * @throws Exception
     */
    public boolean print(@NonNull File file, @NonNull String username, @NonNull String fileName) throws Exception {
        byte[] bytes = Files.readAllBytes(file.toPath());
        PrintJob printJob = new PrintJob.Builder(bytes).jobName(String.format("for user %s file %s", username, fileName)).build();
        var printRequestResult = cupsPrinter.print(printJob);
        log.info(String.format("print document %s for user %s", fileName, username));
        return printRequestResult.isSuccessfulResult();
    }


    private URL url;
    /**
     * check cups host availability
     * @return true if cups host is up
     */
    public boolean checkAlive(){
        try {
            if (url == null){
                url = new URL(String.format("http://%s:" + Config.getCupsPort(), Config.getCupsHost()));
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200){
                log.info("check cups server alive: cups server online");
                return true;
            }
        } catch (IOException e) {
            log.warn("failed to check cups server alive", e);
        }
        log.info("check cups server alive: cups server offline");
        return false;
    }

}
