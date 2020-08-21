package ru.AlexeyFedechkin.mutliSV.telegramBot.Vk;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.PrintQue;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.SpringContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {

    private static final Logger LOG = LoggerFactory.getLogger(CallbackApiLongPollHandler.class);
    private final PrintQue printQue;
    private VkApiClient vkApiClient;
    private GroupActor groupActor;
    private final Random random = new Random();

    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor) {
        super(client, actor);
        this.vkApiClient = client;
        this.groupActor = actor;
        this.printQue = SpringContext.getBean(PrintQue.class);
    }

    public void messageNew(Integer groupId, String secret,  Message message) {
        LOG.info("messageNew: " + message.toString());
        if (message.getAttachments() != null){
            for (MessageAttachment attachment : message.getAttachments()){
                if (attachment.getType() == MessageAttachmentType.DOC){
                    Doc doc = attachment.getDoc();
                    if (doc.getExt().equals("pdf")){

                    } else if (doc.getExt().equals("docx")){

                    }
                    System.out.println(doc.getUrl());
                }
            }
        }
        try {
            vkApiClient.messages().send(groupActor).chatId(random.nextInt()).message("sdf").execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    private static File download(String docUrl, String ext) throws IOException {
        File result = File.createTempFile("vkDownloaded", ext);
        result.deleteOnExit();
        FileUtils.copyURLToFile(new URL(docUrl), result);
        return result;
    }


}
