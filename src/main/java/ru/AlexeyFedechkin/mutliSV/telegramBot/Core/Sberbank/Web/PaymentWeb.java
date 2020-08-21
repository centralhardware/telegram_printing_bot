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
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.User;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.UserType;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service.PaymentService;

import java.util.Optional;

/**
 * endpoint necessary to work with the payment system
 */
@RestController
@RequestMapping()
public class PaymentWeb {

    private static final Logger log = LoggerFactory.getLogger(PaymentWeb.class);

    private final PaymentService paymentService;
    private final Cups cups;
    private final PrintQue printQue;

    public PaymentWeb(PaymentService paymentService, Cups cups, PrintQue printQue) {
        this.paymentService = paymentService;
        this.cups = cups;
        this.printQue = printQue;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> index(){
        log.info("requesting index requesting");
        return ResponseEntity.ok(INDEX_PAGE);
    }

    /**
     * redirection to this endpoint will be performed if the payment was successful
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public ResponseEntity<?> fail(@RequestParam String orderId) {
        log.info("fail page requesting");
        Optional<Payment> paymentOptional = paymentService.findByOrderId(orderId);
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.setIsSuccessfully(false);
            paymentService.save(payment);
            printQue.removeFromQue(String.valueOf(payment.getUuid()));
            return ResponseEntity.ok(FAIL_PAGE);
        } else {
            return ResponseEntity.ok(NO_TRANSACTION_PAGE);
        }
    }

    /**
     * redirection to this endpoint will be performed if the payment was successful
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public ResponseEntity<?> success(@RequestParam String orderId){
        log.info("success page requesting");
        Optional<Payment> paymentOptional = paymentService.findByOrderId(orderId);
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.setIsSuccessfully(true);
            paymentService.save(payment);
            PrintDetail printDetail = null;
            if (payment.getUserType() == UserType.TELEGRAM){
                printDetail = printQue.getPrintDetail(String.valueOf(payment.getUuid()));
            }
            try {
                boolean result = cups.print(printDetail.getFile(), payment.getCreatedByTelegram().getUsername(), printDetail.getOriginalFileName());
                if (payment.getUserType() == UserType.TELEGRAM){
                    printQue.removeFromQue(String.valueOf(payment.getUuid()));
                }
                if (!result){
                    log.warn("print is not be successful");
                    return ResponseEntity.ok(SUCCESS_PAGE_PRINT_FAIL);
                }
            } catch (Exception e) {
                log.warn("unable to print document", e);
                return ResponseEntity.ok(SUCCESS_PAGE_PRINT_FAIL);
            }
            return ResponseEntity.ok(SUCCESS_PAGE);
        } else {
            return ResponseEntity.ok(NO_TRANSACTION_PAGE);
        }
    }

    private static final String NO_TRANSACTION_PAGE =
            "<center><h1>Транзакции с таким номером не существует</h1>" +
                    "<h2>Если вы нашли баг пожайлуста сообщите нам на printomat@centralhardware.ru</h2></center>" +
            "";

    private static final String FAIL_PAGE =
            "<center><H1>Произошла ошибка</H1>" +
            "<h3>Удостоверьтесь, что у вас достаточно средств на карте и повторите процесс</h3>" +
            "</center>" ;

    private static final String SUCCESS_PAGE_PRINT_FAIL =
            "<center> <H1>Оплата прошла успешно. Однако печать не удалась</H1> " +
            "<H3>Приносим свои извенения. Во время отправки на печать произвошла ошибка</H3>" +
            "</center>" +
            "<center>" + Config.getEmbeddedMapIframe() + "</center>";

    private static final String SUCCESS_PAGE =
            "<center> <H1>Оплата прошла успешно</H1> " +
            "<H3>Благодарим за покупку в " + Config.getCompanyName() + "</H3>" +
            "<H3>Документ уже отправлен на печать. Вы можете забрать его по адресу: "+ Config.getCompanyLocation() + "</H3>" +
            "</center>" +
            "<center>" + Config.getEmbeddedMapIframe() + "</center>";

    private static final String INDEX_PAGE =
            "<center><h1>printomat</h1>" +
                    "<h2>Вы попали на служебный домен payment.printomat.online</h2>" +
                    "<h2>Он используется для работы с платежной системой и не предназначен для посещения </h2>" +
                    "</center>";

}
