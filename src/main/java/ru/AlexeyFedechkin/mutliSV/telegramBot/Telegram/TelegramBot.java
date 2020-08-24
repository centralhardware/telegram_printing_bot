package ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram;

import com.google.zxing.NotFoundException;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.Exception;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.PrintDetail;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.PrintQue;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.DocxToPdf;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.QR;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank.PaymentDetail;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank.Sberbank;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.PaymentService;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.UserService;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.SpringContext;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.Command.DiscountCardCommand;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Telegram.Command.StartCommand;

import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TelegramBot extends TelegramLongPollingCommandBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    static {
        ApiContextInitializer.init();
    }

    public static TelegramBot initTelegramBot(){
        TelegramBot telegramBot = null;
        try {
            telegramBot = new TelegramBot();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            botsApi.registerBot(telegramBot);
            log.info("register telegram bot");
        } catch (TelegramApiRequestException e) {
            log.error("failed to register bot", e);
            System.exit(100);
        }
        return telegramBot;
    }

    public TelegramBot(){
        register(new StartCommand());
        log.info(String.format("register command %s", StartCommand.class.toString()));
        if (Config.isIsEnableQr()){
            register(new DiscountCardCommand());
            log.info(String.format("register command %s", DiscountCardCommand.class.toString()));
        }
        service = SpringContext.getBean(UserService.class);
        printQue = SpringContext.getBean(PrintQue.class);
        cups = SpringContext.getBean(Cups.class);
        paymentService = SpringContext.getBean(PaymentService.class);
    }

    private final PaymentService paymentService;
    private final UserService service;
    private final PrintQue printQue;
    private final Cups cups;

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()){
            service.create(Mapper.toInnerUser(update.getMessage().getFrom()));
            var message = update.getMessage();
            if (message.hasPhoto() && Config.isIsEnableQr()){
                processIncomingQR(update);
            } else if (message.hasDocument()){
                processDocument(message.getDocument(), update.getMessage().getChatId(), update.getMessage().getFrom().getUserName());
            }
        } else if (update.hasCallbackQuery()){
            service.create(Mapper.toInnerUser(update.getCallbackQuery().getFrom()));
            if (update.hasCallbackQuery()) {
                processCallback(update.getCallbackQuery());
            }
        }
    }

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    private void processDocument(Document document, Long chatID, String username){
        try{
            SendChatAction sendChatAction = new SendChatAction().setChatId(chatID).setAction(ActionType.UPLOADDOCUMENT);
            execute(sendChatAction);
            if (document.getMimeType().equals(MimeType.DOCX)){
                Runnable task = () -> {
                    try {
                        java.io.File result = null;
                        try {
                            result = DocxToPdf.convert(download(document.getFileId()));
                        } catch (Exception | BootstrapException e) {
                            log.warn("failed to convert docx to pdf", e);
                        }
                        SendDocument sendDocument = new SendDocument().
                                setChatId(chatID).
                                setDocument(String.format("%s.pdf", document.getFileName()), new FileInputStream(result));
                        PdfReader pdfReader = new PdfReader(result.getAbsolutePath());
                        int pageCount = pdfReader.getNumberOfPages();
                        int price =  pageCount * Config.getPagePrice();
                        Payment payment = paymentService.createPayment(service.getUserById(chatID).get() , price);
                        var message = InlineKeyboardBuilder.
                                create().
                                setChatId(chatID).
                                setText(String.format("Поверьте документ \"%s\" на предмет ошибок. '\n " +
                                        pageCount + " л.\n" +
                                        "Стоимость заказа: %s \n  " +
                                        "Оплатить", document.getFileName() ,price)).
                                row().button("да", payment.getUuid()).endRow().
                                row().button("нет", "нет").endRow();
                        printQue.addToQue(payment.getUuid() ,new PrintDetail(result, document.getFileName(), price));
                        execute(sendDocument);
                        execute(message.build());
                    } catch (IOException | TelegramApiException e) {
                        log.warn("", e);
                    }
                };
                executor.execute(task);
            } else if (document.getMimeType().equals(MimeType.PDF)){
                sendChatAction = new SendChatAction().setChatId(chatID).setAction(ActionType.TYPING);
                execute(sendChatAction);
                InputStream inputStream = download(document.getFileId());
                java.io.File targetFile = java.io.File.createTempFile("download_pdf", ".pdf");
                OutputStream outStream = new FileOutputStream(targetFile);

                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                PdfReader pdfReader = new PdfReader(targetFile.getAbsolutePath());
                int pageNumber = pdfReader.getNumberOfPages();
                int price =pageNumber* Config.getPagePrice();
                Payment payment = paymentService.createPayment(service.getUserById(chatID).get() , price);
                PaymentDetail paymentDetail = Sberbank.requestPaymentDetail(payment);
                payment.setOrderId(paymentDetail.getOrderId());
                paymentService.save(payment);
                printQue.addToQue(String.valueOf(payment.getUuid()) ,new PrintDetail(targetFile, document.getFileName(), price));
                if (!cups.checkAlive()){
                    SendMessage sendMessage = new SendMessage().setChatId(chatID).setText("Приносим свои извенения, но мы не можем выполнить печать в данный момент");
                    execute(sendMessage);
                    return;
                }
                InlineKeyboardBuilder inlineKeyboardBuilder = InlineKeyboardBuilder.create().
                        setText(String.format(pageNumber + " л.\n" +
                                "Стоимость печати файла \"%s\" сотавляет: %s руб.", document.getFileName(), price)).
                        setChatId(chatID).
                        row().button("Оплатить", "sdf", paymentDetail.getFormUrl()).endRow();
                execute(inlineKeyboardBuilder.build());
            }
        } catch (java.lang.Exception e) {
            log.warn("", e);
        }
    }

    private void processCallback(@NonNull CallbackQuery callbackQuery){
        log.info(String.format("processing callback for user %s", callbackQuery.getFrom().getUserName()));
        try {
            SendChatAction sendChatAction = new SendChatAction().setChatId(callbackQuery.getMessage().getChatId()).setAction(ActionType.TYPING);
            execute(sendChatAction);
            if (callbackQuery.getData().equals("нет")){
                DeleteMessage deleteMessage = new DeleteMessage().
                        setChatId(callbackQuery.getMessage().getChatId()).
                        setMessageId(callbackQuery.getMessage().getMessageId());
                execute(deleteMessage);
                SendMessage sendMessage = new SendMessage().setText("Покупка отменена").setChatId(callbackQuery.getMessage().getChatId());
                execute(sendMessage);
                printQue.removeFromQue(service.getToken(Long.valueOf(callbackQuery.getFrom().getId())));
                return;
            }
            if (printQue.isFileInQue(callbackQuery.getData())){
                DeleteMessage deleteMessage = new DeleteMessage().
                        setChatId(callbackQuery.getMessage().getChatId()).
                        setMessageId(callbackQuery.getMessage().getMessageId());
                execute(deleteMessage);
                Payment payment = paymentService.findByUuid(callbackQuery.getData()).get();
                PaymentDetail paymentDetail = Sberbank.requestPaymentDetail(payment);
                payment.setOrderId(paymentDetail.getOrderId());
                paymentService.save(payment);
                if (!cups.checkAlive()){
                    SendMessage sendMessage = new SendMessage().setChatId(String.valueOf(callbackQuery.getFrom().getId())).setText("Приносим свои извенения, но мы не можем выполнить печать в данный момент");
                    execute(sendMessage);
                    return;
                }
                InlineKeyboardBuilder inlineKeyboardBuilder = InlineKeyboardBuilder.create().
                        setText(printQue.getPrintDetail(callbackQuery.getData()).getOriginalFileName()).
                        setChatId(Long.valueOf(callbackQuery.getFrom().getId())).
                        row().button("Оплатить", "sdf", paymentDetail.getFormUrl()).endRow();
                execute(inlineKeyboardBuilder.build());
                return;
            }
            var userService = SpringContext.getBean(UserService.class);
            var userOptional =  userService.getUserByToken(callbackQuery.getData());
            if (userOptional.isPresent()){
                var user = userOptional.get();
                user.incrementNumberOfPurchases();
                userService.update(user);
                DeleteMessage deleteMessage = new DeleteMessage().
                        setChatId(callbackQuery.getMessage().getChatId()).
                        setMessageId(callbackQuery.getMessage().getMessageId());
                SendMessage sendMessage = new SendMessage().setChatId(callbackQuery.getMessage().getChatId()).setText("покупка отмечена");
                execute(deleteMessage);
                log.info(String.format("delete InlineKeyboardBoard for user %s", callbackQuery.getFrom().getUserName()));
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            log.warn("failed to send message", e);
        } catch (java.lang.Exception e) {
            log.warn("", e);
        }
    }

    private void processIncomingQR(@NonNull Update update){
        try {
            SendChatAction sendChatAction = new SendChatAction().setChatId(update.getMessage().getChatId()).setAction(ActionType.TYPING);
            execute(sendChatAction);
            String token = null;
            SendMessage sendMessage = new SendMessage().
                    setChatId(update.getMessage().getChatId());
            try {
                token = QR.readQRCodeImage(download(update.getMessage().getPhoto()));
            } catch (NotFoundException e) {
                log.warn("failed to scan QR code", e);
                sendMessage.setText("QR код не найден");
            } catch (IOException e) {
                log.warn("", e);
                sendMessage.setText("Не известная ошибка");
            } catch (TelegramApiException e) {
                log.warn("failed to download photo from telegram", e);
                sendMessage.setText("Telegram error");
            }
            var optionalUser = service.getUserByToken(token);
            if (optionalUser.isPresent()){
                sendMessage = InlineKeyboardBuilder.
                        create().
                        setChatId(update.getMessage().getChatId()).
                        setText(optionalUser.get().toString()).
                        row().button("отметить покупку", token).endRow().
                        build();
            } else {
                sendMessage.setText("пользователь не найден");
            }
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("failed to send message", e);
        }
    }

    public InputStream download(@NotNull List<PhotoSize> photoSizes) throws TelegramApiException {
        PhotoSize photoSize = photoSizes.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
        String path;
        if (photoSize.hasFilePath()) {
            path = photoSize.getFilePath();
        } else {
            path = getFilePath(photoSize.getFileId());
        }
        return downloadFileAsStream(path);
    }

    public InputStream download(String fileID) throws TelegramApiException {
        return downloadFileAsStream(getFilePath(fileID));
    }

    public String getFilePath(String fileID){
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(fileID);
        try {
            File file = execute(getFileMethod);
            return  file.getFilePath();
        } catch (TelegramApiException e) {
            log.warn("", e);
        }
        return null;
    }

    /**
     * send message
     * @param text text
     * @param chatId chatId
     */
    public void sendMessage(String text, Long chatId){
        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("failed to send message", e);
        }
    }

    /**
     * send photo
     * @param text caption
     * @param chatId chatId
     * @param file document
     * @param fileName name of document
     */
    public void  sendMessage(String text, Long chatId, java.io.File file, String fileName){
        try {
            SendDocument sendPhoto = new SendDocument().setChatId(chatId).setCaption(text).setDocument(fileName,new FileInputStream(file.getAbsolutePath()));
            execute(sendPhoto);
        } catch (TelegramApiException | FileNotFoundException e) {
            log.warn("failed to send message", e);
        }
    }

    public String getBotUsername() {
        return Config.getTelegramUsername();
    }

    public String getBotToken() {
        return Config.getTelegramToken();
    }

}
