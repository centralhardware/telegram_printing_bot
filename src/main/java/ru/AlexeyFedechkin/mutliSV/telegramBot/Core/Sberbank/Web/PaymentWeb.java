package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank.Web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Config;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.PrintDetail;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.PrintQue;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.Payment;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.PaymentService;

import java.util.Optional;

@RestController
@RequestMapping()
public class PaymentWeb {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    private final PaymentService paymentService;
    private final Cups cups;
    private final PrintQue printQue;

    public PaymentWeb(PaymentService paymentService, Cups cups, PrintQue printQue) {
        this.paymentService = paymentService;
        this.cups = cups;
        this.printQue = printQue;
    }


    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public ResponseEntity<?> fail(@RequestParam String orderId) throws Exception {
        log.info("fail page requesting");
        Optional<Payment> paymentOptional = paymentService.findByOrderId(orderId);
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.setIsSuccessfully(false);
            paymentService.save(payment);
            printQue.removeFromQue(String.valueOf(payment.getCreatedBy().getId()));
            return ResponseEntity.ok("<center><H1>Произошла ошибка</H1>" +
                    "<h3>Удостоверьтесь, что у вас достаточно средств на карте и повторите процесс</h3>" +
                    "</center>");
        } else {
            return ResponseEntity.ok("Транзакции с таким номером не существует");
        }
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public ResponseEntity<?> success(@RequestParam String orderId){
        log.info("success page requesting");
        Optional<Payment> paymentOptional = paymentService.findByOrderId(orderId);
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.setIsSuccessfully(true);
            paymentService.save(payment);
            PrintDetail printDetail = printQue.getPrintDetail(String.valueOf(payment.getCreatedBy().getId()));
            try {
                cups.print(printDetail.getFile(), payment.getCreatedBy().getUsername(), printDetail.getOriginalFileName());
            } catch (Exception e) {
                return ResponseEntity.ok("<center> <H1>Оплата прошла успешно. Однако печать не удалась</H1> " +
                        "<H3>Приносим свои извенения. Во время отправки на печать произвошла ошибка</H3>" +
                        "</center>" +
                        "<center>" + Config.getEmbeddedMapIframe() + "</center>");
            }
            return ResponseEntity.ok("<center> <H1>Оплата прошла успешно</H1> " +
                    "<H3>Благодарим за покупку в " + Config.getCompanyName() + "</H3>" +
                    "<H3>Документ уже отправлен на печать. Вы можете забрать его по адресу: "+ Config.getCompanyLocation() + "</H3>" +
                    "</center>" +
                    "<center>" + Config.getEmbeddedMapIframe() + "</center>");
        } else {
            return ResponseEntity.ok("Транзакции с таким номером не существует");
        }
    }

}
