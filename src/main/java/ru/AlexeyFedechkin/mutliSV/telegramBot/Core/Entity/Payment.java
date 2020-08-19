package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Builder
@NoArgsConstructor
public class Payment {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String uuid;
    private String orderId;
    @Column(nullable = false)
    private Integer amount;
    @Column
    private Boolean isSuccessfully;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @ManyToOne
    @JoinColumn(name = "createdBy_id",nullable = false)
    private  TelegramUser createdBy;

    public void setIsSuccessfully(boolean isSuccessfully){
        log.info(String.format("set if success to %s for transaction %s",isSuccessfully, uuid ));
        this.isSuccessfully = isSuccessfully;
    }

    public Payment(String uuid, String orderId, Integer amount, Boolean isSuccessfully, Date createDate, TelegramUser createdBy) {
        this.uuid = uuid;
        this.orderId = orderId;
        this.amount = amount;
        this.isSuccessfully = isSuccessfully;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Boolean getSuccessfully() {
        return isSuccessfully;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public TelegramUser getCreatedBy() {
        return createdBy;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
