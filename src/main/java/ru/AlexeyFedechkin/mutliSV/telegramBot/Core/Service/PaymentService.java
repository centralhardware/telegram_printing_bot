package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository.PaymentRepository;

import java.util.Optional;

@Component
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    @Autowired
    private PaymentRepository repository;

    public Payment createPayment(TelegramUser user, int amount){
        Payment payment = Payment.builder().amount(amount).createdBy(user).build();
        return repository.save(payment);
    }

    public Optional<Payment> findByOrderId(String orderId){
        return repository.findByOrderId(orderId);
    }

    public void save(Payment payment){
        repository.save(payment);
    }

}
