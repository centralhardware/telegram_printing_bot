package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sberbank {

    private static final Logger log = LoggerFactory.getLogger(Sberbank.class);

    /**
     * generate URL for register payment
     * @param orderNumber
     * @param amount
     * @return
     */
    public static String getRegisterUrl(String orderNumber, int amount){
        // cart generation
        String cartItem = new JSONObject().put("cartItems",
                new JSONObject().put("items",
                        new JSONArray().put(new JSONObject().
                                put("positionId", "1").
                                put("name", "печать").
                                put("quantity", new JSONObject().
                                        put("value", amount / Config.getPagePrice()).
                                        put("measure", "страница")).
                                put("itemAmount", toMinimumUnit(amount)).
                                put("itemCode", "1").
                                put("tax", new JSONObject().
                                        // 0 – без НДС;
                                        //1 – НДС по ставке 0%;
                                        //2 – НДС чека по ставке 10%;
                                        //3 – НДС чека по ставке 18%;
                                        //4 – НДС чека по расчетной ставке 10/110;
                                        //5 – НДС чека по расчетной ставке 18/118.
                                        put("taxType", "1").
                                        put("taxSum", 0)).
                                put("itemPrice", toMinimumUnit(Config.getPagePrice()))))).toString();

        return new URIBuilder().
                setScheme("https").
                setHost("3dsec.sberbank.ru").
                setPath("payment/rest/register.do").
                setParameter("userName", Config.getSberbankUserName()).
                setParameter("password", Config.getSberbankPassword()).
                setParameter("orderNumber", orderNumber).
                setParameter("amount", String.valueOf(toMinimumUnit(amount))).
                setParameter("returnUrl", Config.getSberbankSuccessUrl()).
                setParameter("failUrl", Config.getSberbankFailUrl()).
                setParameter("orderBundle", cartItem).
                setParameter("description", "Оплата печати").toString();
    }

    /**
     * @param amount amount in rouble
     * @return amount in kopecks
     */
    private static int toMinimumUnit(int amount){
        return amount * 100;
    }

    /**
     * execute register.do request and get PaymentDetail
     * @param payment transaction
     * @return payment detail for giving payment
     */
    public static PaymentDetail requestPaymentDetail(Payment payment){
        try{
            String registerUrl = Sberbank.getRegisterUrl(payment.getUuid(), payment.getAmount());
            URL url = new URL(registerUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            JSONObject json = new JSONObject(content.toString());
            return new PaymentDetail(json.getString("orderId"), json.getString("formUrl"));
        } catch (IOException e) {
            log.warn("failed to request payment detail", e);
            return null;
        }
    }


}
