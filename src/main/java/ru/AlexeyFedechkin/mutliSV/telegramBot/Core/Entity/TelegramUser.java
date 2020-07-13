package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * data for telegram user
 */
@Entity
@Getter
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TelegramUser {
    @Id
    private String username;
    private String firstName;
    private String secondName;
    @Column(length = 64)
    @Setter
    private String token = "";
    private int numberOfPurchases;

    public void incrementNumberOfPurchases(){
       log.info(String.format("increment numberOfPurchases for user %s", username));
        numberOfPurchases++;
    }

    @Override
    public String toString() {
        return String.format("username: %s \n" +
                "имя: %s \n" +
                "фамилия: %s \n" +
                "количество покупок: %s \n",
                username, firstName, secondName, numberOfPurchases);
    }

}