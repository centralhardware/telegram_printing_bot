package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
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

    private static final Logger log = LoggerFactory.getLogger(TelegramUser.class);

    /**
     * chat id
     */
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


    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Set<Payment> getPayments() {
        return new HashSet<>(payments);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }
}