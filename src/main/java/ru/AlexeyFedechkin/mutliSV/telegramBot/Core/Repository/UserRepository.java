package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.TelegramUser;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<TelegramUser, Long> {

    Optional<TelegramUser> findByToken(String token);

}
