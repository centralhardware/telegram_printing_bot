package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Random;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository.UserRepository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.TokenNotFoundException;

import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(@NonNull TelegramUser user){
        if (!checkExistence(user.getUsername())){
            userRepository.save(user);
            log.info(String.format("create user: %s", user.getUsername()));
        }
    }

    public void update(@NonNull TelegramUser user){
        userRepository.save(user);
        log.info(String.format("update user: %s", user.getUsername()));
    }

    /**
     * @param username
     * @return true if user exist
     */
    private boolean checkExistence(@NonNull String username){
        return userRepository.existsById(username);
    }

    /**
     * get or generate token by user username
     * @param username
     * @return
     * @throws TokenNotFoundException requested user not exist
     */
    public String getToken(@NonNull String username) throws TokenNotFoundException {
        var optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()){
            var user = optionalUser.get();
            if (user.getToken() == null){
                var token = Random.getAlphaNumericString();
                user.setToken(token);
                userRepository.save(user);
                log.info(String.format("generate token - %s for user %s", token, username));
                return token;
            } else {
                log.info(String.format("get token for user %s from database", username));
                return user.getToken();
            }
        }
        throw new TokenNotFoundException();
    }

    public Optional<TelegramUser> getUserByToken(@NonNull String token){
        return userRepository.findByToken(token);
    }

    public Optional<TelegramUser> getUserByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

}