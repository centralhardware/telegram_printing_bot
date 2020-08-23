package ru.AlexeyFedechkin.mutliSV.telegramBot.Vk;

import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.VkUserService;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.SpringContext;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.Command.StartCommand;

public class VkBot {

    private static final Logger log = LoggerFactory.getLogger(VkBot.class);

    public static void initVkBot() throws Exception {
        log.info("register vk bot");
        new VkBot();
    }

    private final VkUserService service;

    public VkBot() {
        this.service = SpringContext.getBean(VkUserService.class);
        Group group = new Group(Config.getVkGroupId() ,Config.getVkGroupToken());
        group.onEveryMessage(message -> service.create(message.authorId()));
        group.onSimpleTextMessage(message -> {
            if (message.getText().equals("начать")){
                new Message().
                        from(group).
                        to(message.authorId()).
                        text(StartCommand.HELLO_MESSAGE).
                        send();
            }
        });
        group.onDocMessage(message -> {

        });
    }

}
