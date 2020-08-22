package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.UserType;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository.PaymentRepository;

import java.util.Optional;

@Component
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }


    public Payment createPayment(TelegramUser user, int amount){
        Payment payment = Payment.builder().amount(amount).createdByTelegram(user).userType(UserType.TELEGRAM).build();
        return repository.save(payment);
    }

    public Optional<Payment> findByOrderId(String orderId){
        return repository.findByOrderId(orderId);
    }

    public Optional<Payment> findByUuid(String uuid){
        return  repository.findByUuid(uuid);
    }

    public void save(Payment payment){
        repository.save(payment);
    }

}
