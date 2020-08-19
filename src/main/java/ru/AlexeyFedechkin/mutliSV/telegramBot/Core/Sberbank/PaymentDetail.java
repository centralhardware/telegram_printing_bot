package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank;

public class PaymentDetail {

    private final String orderId;
    private final String formUrl;

    public PaymentDetail(String orderId, String formUrl) {
        this.orderId = orderId;
        this.formUrl = formUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFormUrl() {
        return formUrl;
    }
}
