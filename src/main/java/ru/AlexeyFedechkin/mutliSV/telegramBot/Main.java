package ru.AlexeyFedechkin.mutliSV.telegramBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.TelegramBot;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Vk.VkBot;

@SpringBootApplication
@ComponentScan("ru.AlexeyFedechkin.mutliSV.telegramBot")
@EnableJpaRepositories("ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository")
@EnableAutoConfiguration
@Configuration
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
        TelegramBot.initTelegramBot();
        if (Config.isIsEnableVk()){
            VkBot.initVkBot();
        }
    }

}