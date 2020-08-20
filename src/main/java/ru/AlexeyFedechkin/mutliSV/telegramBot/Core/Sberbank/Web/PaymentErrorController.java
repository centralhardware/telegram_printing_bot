package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Sberbank.Web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * handle 400 and 404
 */
@Controller
public class PaymentErrorController implements ErrorController {

    @RequestMapping(value = "error", method = RequestMethod.GET)
    public ResponseEntity<?> handleError(HttpServletRequest httpRequest) {

        String errorMsg = "";
        int httpErrorCode = (int) httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "<center><h1>Request param orderId not found</h1>" +
                        "<h2>Если вы нашли баг пожайлуста сообщите нам на printomat@centralhardware.ru</h2></center>";
                break;
            }
            case 404: {
                errorMsg = "<center><h1><a href='https://ru.wikipedia.org/wiki/%D0%9E%D1%88%D0%B8%D0%B1%D0%BA%D0%B0_404'>404</a></h1>" +
                        "<h2>Поисковая команда выехала на поиски. Ожидайте</h2>\n" +
                        "<h2>Если вы нашли баг пожайлуста сообщите нам на printomat@centralhardware.ru</h2></center>";
                break;
            }
            default:{
                errorMsg = "Неизвестная ошибка";
            }
        }
        return ResponseEntity.ok(errorMsg);
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
