package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;

@Component
@Scope(scopeName = "singleton")
@Slf4j
public class Cups {

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

}
