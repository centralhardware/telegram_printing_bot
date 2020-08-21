package ru.AlexeyFedechkin.mutliSV.telegramBot.Vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;

public class VkBot {

    private static final Logger log = LoggerFactory.getLogger(VkBot.class);

    public static void initVkBot() throws Exception {
        log.info("register vk bot");
        new VkBot();
    }

    public VkBot() throws Exception {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vkApiClient = new VkApiClient(transportClient);
        GroupActor groupActor = new GroupActor(Config.getVkGroupId(), Config.getVkGroupToken());
        try {
            vkApiClient.groups().setLongPollSettings(groupActor, Config.getVkGroupId()).messageNew(true).apiVersion("5.5").enabled(true).execute();
        } catch (ApiException e) {
            throw new RuntimeException("Api error during init", e);
        } catch (ClientException e) {
            throw new RuntimeException("Client error during init", e);
        }
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vkApiClient, groupActor);
        handler.run();
    }

}
