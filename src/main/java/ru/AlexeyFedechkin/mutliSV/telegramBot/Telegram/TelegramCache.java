package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component()
@Scope(scopeName = "singleton")
@Slf4j
public class TelegramCache {

    private final Map<Integer, String> cache = new ConcurrentHashMap<>();

    public void store(@NonNull Integer userId, @NonNull String fileID){
        log.info(String.format("put value %d into key %s", userId, fileID));
        cache.put(userId, fileID);
    }

    public String get(@NonNull Integer userID){
        return cache.get(userID);
    }

}
