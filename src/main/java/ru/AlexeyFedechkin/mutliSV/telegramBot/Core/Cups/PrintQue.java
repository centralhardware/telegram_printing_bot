package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups;

import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(scopeName = "singleton")
public class PrintQue {

    private final Map<String, PrintDetail> que = new ConcurrentHashMap<>();

    public void addToQue(@NonNull String id, @NonNull PrintDetail printDetail){
        que.put(id, printDetail);
    }

    /**
     * get detail needed for printing
     * @param id user id
     * @return printDetail
     */
    public PrintDetail getPrintDetail(@NonNull String id){
        return que.get(id);
    }

    public boolean isFileInQue(@NonNull String id){
        return que.containsKey(id);
    }

    public void removeFromQue(@NonNull String id){
        que.remove(id);
    }


}
