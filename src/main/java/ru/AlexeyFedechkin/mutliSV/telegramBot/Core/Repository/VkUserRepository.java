package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.VkUser;

@Repository
public interface VkUserRepository extends CrudRepository<VkUser, Integer> {
}
