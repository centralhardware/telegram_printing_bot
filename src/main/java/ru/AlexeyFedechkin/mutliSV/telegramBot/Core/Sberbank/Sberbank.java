package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

@Slf4j
public class Sberbank {

    public static String getRegisterUrl(String orderNumber, int amount){
        String cartItem = new JSONObject().put("cartItems",
                new JSONObject().put("items",
                        new JSONArray().put(new JSONObject().
                                put("positionId", "1").
                                put("name", "печать").
                                put("quantity", new JSONObject().
                                        put("value", amount / Config.getPagePrice()).
                                        put("measure", "страница")).
                                put("itemAmount", amount * 100).
                                put("itemCode", "1").
                                put("tax", new JSONObject().
                                        put("taxType", "1").
                                        put("taxSum", 0)).
                                put("itemPrice", Config.getPagePrice() * 100)))).toString();
        return new URIBuilder().
                setScheme("https").
                setHost("3dsec.sberbank.ru").
                setPath("payment/rest/register.do").
                setParameter("userName", Config.getSberbankUserName()).
                setParameter("password", Config.getSberbankPassword()).
                setParameter("orderNumber", orderNumber).
                setParameter("amount", String.valueOf(amount * 100)).
                setParameter("returnUrl", Config.getSberbankSuccessUrl()).
                setParameter("failUrl", Config.getSberbankFailUrl()).
                setParameter("orderBundle", cartItem).
                setParameter("description", "Оплата печати").toString();
    }

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
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            JSONObject json = new JSONObject(content.toString());
            return new PaymentDetail(json.getString("orderId"), json.getString("formUrl"));
        } catch (IOException e) {
            log.warn("", e);
            return null;
        }
    }


}
