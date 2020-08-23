package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table
public class VkUser {

    @Id
    private Integer id;

    @OneToMany(mappedBy = "createdByVk")
    private Set<Payment> payments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
