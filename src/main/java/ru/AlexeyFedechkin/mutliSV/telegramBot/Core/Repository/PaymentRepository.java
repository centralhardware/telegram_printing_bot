package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByUuid(String uuid);

}
