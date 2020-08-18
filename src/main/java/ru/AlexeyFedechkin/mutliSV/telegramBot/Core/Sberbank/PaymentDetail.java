package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank;

public class PaymentDetail {

    private String orderId;
    private String formUrl;

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
