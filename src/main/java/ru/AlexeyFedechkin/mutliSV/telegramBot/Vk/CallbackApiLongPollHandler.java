package ru.AlexeyFedechkin.mutliSV.telegramBot.Vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {

    private static final Logger LOG = LoggerFactory.getLogger(CallbackApiLongPollHandler.class);


    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor);
    }

    public void messageNew(Integer groupId, Message message) {
        LOG.info("messageNew: " + message.toString());
        if (message.getAttachments() != null){
            MessageAttachment attachment = message.getAttachments().get(0);
        }
    }


}
