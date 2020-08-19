package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;

public class Mapper {

    public static TelegramUser toInnerUser(@NonNull User user){
        return TelegramUser.builder().
                id(Long.valueOf(user.getId())).
                firstName(user.getFirstName()).
                secondName(user.getLastName()).
                username(user.getUserName()).
                build();
    }

}
