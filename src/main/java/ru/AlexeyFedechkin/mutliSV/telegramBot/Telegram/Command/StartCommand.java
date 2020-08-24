package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.UserService;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.SpringContext;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.Mapper;

public class StartCommand extends BotCommand {

    private static final Logger log = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand(){
        super("start","show start message");
    }

    public static final String HELLO_MESSAGE = "Привет %s Добро пожаловать в бот " + Config.getCompanyName() +   "\n" +
            " просто отправте боту файл в формате docx или pdf и можете забрать готовую распечатку в "+ Config.getCompanyLocation() + "\n" +
            "Мы работаем: " + Config.getWorkingTime();

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments){
        log.info(String.format("execute command %s for user %s", StartCommand.class.toString(), user.getUserName()));
        try{
            SendChatAction sendChatAction = new SendChatAction().setChatId(chat.getId()).setAction(ActionType.TYPING);
            absSender.execute(sendChatAction);
            UserService service = SpringContext.getBean(UserService.class);
            service.create(Mapper.toInnerUser(user));
            SendMessage sendMessage = new SendMessage().
                    setChatId(chat.getId()).
                    setText(String.format(HELLO_MESSAGE, user.getUserName()));
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("failed to send message", e);
        }
    }
}