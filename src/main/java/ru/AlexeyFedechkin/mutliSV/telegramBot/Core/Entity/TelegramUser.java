package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;

import javax.persistence.*;
import java.util.Set;

/**
 * data for telegram user
 */
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    @Id
    private Long id;
    private String username;
    private String firstName;
    private String secondName;
    @Column(length = 64)
    private String token = "";
    private int numberOfPurchases;
    @OneToMany(mappedBy = "createdByTelegram")
    private Set<Payment> payments;

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


    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }
}