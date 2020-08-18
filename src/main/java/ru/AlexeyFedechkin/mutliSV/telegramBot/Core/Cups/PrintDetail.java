package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import java.io.File;

public class PrintDetail {

    public PrintDetail(File file, String originalFileName, int price) {
        this.file = file;
        this.originalFileName = originalFileName;
        this.price = price;
    }

    private final int price;
    private final File file;
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
