package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {

    private Long chatId;
    private String text;

    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;

    private InlineKeyboardBuilder() {}

    public static InlineKeyboardBuilder create() {
        return new InlineKeyboardBuilder();
    }

    public static InlineKeyboardBuilder create(@NonNull Long chatId) {
        InlineKeyboardBuilder builder = new InlineKeyboardBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    public InlineKeyboardBuilder setText(@NonNull String text) {
        this.text = text;
        return this;
    }

    public InlineKeyboardBuilder setChatId(@NonNull Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public InlineKeyboardBuilder row() {
        this.row = new ArrayList<>();
        return this;
    }

    public InlineKeyboardBuilder button(@NonNull String text, @NonNull String callbackData) {
        row.add(new InlineKeyboardButton().setText(text).setCallbackData(callbackData));
        return this;
    }

    public InlineKeyboardBuilder button(@NonNull String text, @NonNull String callbackData, @NonNull String url) {
        row.add(new InlineKeyboardButton().setText(text).setCallbackData(callbackData).setUrl(url));
        return this;
    }

    public InlineKeyboardBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }


    public SendMessage build() {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
