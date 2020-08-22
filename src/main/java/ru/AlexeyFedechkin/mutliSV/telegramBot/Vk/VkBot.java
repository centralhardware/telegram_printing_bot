package ru.AlexeyFedechkin.mutliSV.telegramBot.Vk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VkBot {

    private static final Logger log = LoggerFactory.getLogger(VkBot.class);

    public static void initVkBot() throws Exception {
        log.info("register vk bot");
        new VkBot();
    }

    public VkBot() {

    }

}
