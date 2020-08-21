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
    private Long id;

    @OneToMany(mappedBy = "createdByVk")
    private Set<Payment> payments;

}
