package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
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
    @Column(nullable = false)
    private Integer amount;
    @Column
    private Boolean isSuccessfully;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy_id_telegram")
    private  TelegramUser createdByTelegram;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy_id_vk")
    private VkUser createdByVk;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public void setIsSuccessfully(boolean isSuccessfully){
        log.info(String.format("set if success to %s for transaction %s",isSuccessfully, uuid ));
        this.isSuccessfully = isSuccessfully;
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

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
