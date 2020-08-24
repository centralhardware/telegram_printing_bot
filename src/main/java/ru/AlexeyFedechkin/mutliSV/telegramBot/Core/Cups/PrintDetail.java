package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import java.io.File;

public class PrintDetail {

    public PrintDetail(File file, String originalFileName, int price) {
        this.file = file;
        this.originalFileName = originalFileName;
        this.price = price;
    }

    /**
     * amount in roubles
     */
    private final int price;
    /**
     * temp file to ping
     */
    private final File file;
    /**
     * the name of the file that was originally sent to from the messenger
     */
    private final String originalFileName;

    public int getPrice() {
        return price;
    }

    public File getFile() {
        return file;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }
}
