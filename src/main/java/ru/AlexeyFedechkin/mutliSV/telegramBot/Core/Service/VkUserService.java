package ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Service;

import org.springframework.stereotype.Service;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Entity.VkUser;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Repository.VkUserRepository;

@Service
public class VkUserService {

    private final VkUserRepository repository;

    public VkUserService(VkUserRepository repository) {
        this.repository = repository;
    }

    public void create(Integer id){
        VkUser vkUser = new VkUser();
        vkUser.setId(id);
        repository.save(vkUser);
    }

}
