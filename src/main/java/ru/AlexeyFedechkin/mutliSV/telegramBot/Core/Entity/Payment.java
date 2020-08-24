package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * bank transaction presentation
 */
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private static final Logger log = LoggerFactory.getLogger(Payment.class);

    /**
     * ID unique in the store system
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String uuid;
    /**
     * ID unique in the bank system
     */
    private String orderId;
    /**
     * amount in roubles
     */
    @Column(nullable = false)
    private Integer amount;
    @Column
    private Boolean isSuccessfully;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    /**
     * if receive from telegram
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy_id_telegram")
    private  TelegramUser createdByTelegram;
    /**
     * if receive from vk
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy_id_vk")
    private VkUser createdByVk;
    /**
     * which messenger the file was sent from
     */
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public void setIsSuccessfully(boolean isSuccessfully){
        log.info(String.format("set if success to %s for transaction %s", isSuccessfully, uuid ));
        this.isSuccessfully = isSuccessfully;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getAmount() {
        return amount;
    }

    public TelegramUser getCreatedByTelegram() {
        return createdByTelegram;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getCreateDate() {
        return new Date(createDate.getTime());
    }
}
