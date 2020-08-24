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

    /**
     * @param id payment UUID
     * @param printDetail print detail
     */
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

    /**
     * @param id payment id
     * @return true if printDetail id queued
     */
    public boolean isFileInQue(@NonNull String id){
        return que.containsKey(id);
    }

    /**
     * remove print detail from que after success printing
     * @param id payment id
     */
    public void removeFromQue(@NonNull String id){
        que.remove(id);
    }


}
