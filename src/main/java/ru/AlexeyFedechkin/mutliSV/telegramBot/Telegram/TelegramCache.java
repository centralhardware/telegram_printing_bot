package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component()
@Scope(scopeName = "singleton")
public class TelegramCache {

    private static final Logger log = LoggerFactory.getLogger(TelegramCache.class);

    private final Map<Integer, String> cache = new ConcurrentHashMap<>();

    public void store(@NonNull Integer userId, @NonNull String fileID){
        log.info(String.format("put value %d into key %s", userId, fileID));
        cache.put(userId, fileID);
    }

    public String get(@NonNull Integer userID){
        return cache.get(userID);
    }

}
