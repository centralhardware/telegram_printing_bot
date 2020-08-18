package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Random;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository.UserRepository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.TokenNotFoundException;

import java.util.Optional;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

    @Autowired
    private UserRepository userRepository;

    public void create(@NonNull TelegramUser user){
        if (!checkExistence(user.getId())){
            userRepository.save(user);
            log.info(String.format("create user: %s", user.getUsername()));
        }
    }

    public void update(@NonNull TelegramUser user){
        userRepository.save(user);
        log.info(String.format("update user: %s", user.getUsername()));
    }

    /**
     * @param id
     * @return true if user exist
     */
    private boolean checkExistence(@NonNull Long id){
        return userRepository.existsById(id);
    }

    /**
     * get or generate token by user username
     * @param id
     * @return
     * @throws TokenNotFoundException requested user not exist
     */
    public String getToken(@NonNull Long id) {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            var user = optionalUser.get();
            if (user.getToken() == null){
                var token = Random.getAlphaNumericString();
                user.setToken(token);
                userRepository.save(user);
                log.info(String.format("generate token - %s for user %s", token, id));
                return token;
            } else {
                log.info(String.format("get token for user %s from database", id));
                return user.getToken();
            }
        }
        return null;
    }

    public Optional<TelegramUser> getUserByToken(@NonNull String token){
        return userRepository.findByToken(token);
    }

    public Optional<TelegramUser> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

}