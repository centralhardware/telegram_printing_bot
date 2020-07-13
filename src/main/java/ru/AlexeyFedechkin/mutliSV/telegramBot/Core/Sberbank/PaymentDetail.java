package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank;

/**
 * data that the bank transfers after registration of the payment
 */
public class PaymentDetail {

    private final String orderId;
    /**
     * URL to payment form
     */
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
