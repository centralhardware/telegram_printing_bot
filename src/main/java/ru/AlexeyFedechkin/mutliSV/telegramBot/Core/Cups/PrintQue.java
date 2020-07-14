package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(scopeName = "singleton")
public class PrintQue {

    private final Map<String, PrintFile> que = new ConcurrentHashMap<>();

    public void addToQue(@NonNull String id, @NonNull File file, @NonNull String originalFileName, int price){
        que.put(id, new PrintFile(file, originalFileName, price));
    }

    public boolean isFileInQue(String id){
        return que.containsKey(id);
    }

    public String getOriginalFileName(String id){
        return que.get(id).originalFileName;
    }

    public File getFile(String id){
        return que.get(id).file;
    }

    public int getPrice(String id){
        return que.get(id).price;
    }

    public void removeFromQue(String id){
        que.remove(id);
    }

    static class PrintFile{

        public PrintFile(File file, String originalFileName, int price) {
            this.file = file;
            this.originalFileName = originalFileName;
            this.price = price;
        }

        private final int price;
        private final File file;
        private final String originalFileName;
    }

}
