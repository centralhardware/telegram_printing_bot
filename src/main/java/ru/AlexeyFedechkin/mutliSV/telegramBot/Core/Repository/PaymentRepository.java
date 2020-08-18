package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, String> {

    Optional<Payment> findByOrderId(String orderId);

}
