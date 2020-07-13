package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(scopeName = "singleton")
public class PrintQue {

    private final Map<String, PrintFile> que = new HashMap<>();

    public void addToQue(@NonNull String id, @NonNull File file, @NonNull String originalFileName){
        que.put(id, new PrintFile(file, originalFileName));
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

    public void removeFromQue(String id){
        que.remove(id);
    }

    static class PrintFile{

        public PrintFile(File file, String originalFileName) {
            this.file = file;
            this.originalFileName = originalFileName;
        }

        private final File file;
        private final String originalFileName;
    }

}
