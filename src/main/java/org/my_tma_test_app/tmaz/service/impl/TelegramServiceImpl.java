package org.my_tma_test_app.tmaz.service.impl;

import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.repo.UserRepository;
import org.my_tma_test_app.tmaz.service.TelegramService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TelegramServiceImpl implements TelegramService {

    private final UserRepository userRepository;


    @Override
    public TelegramUser save(TelegramUser user) {
        Optional<TelegramUser> optionalTelegramUser = userRepository.findById(user.getId());
        if (optionalTelegramUser.isEmpty()) {
            return userRepository.save(user);
        }
        return optionalTelegramUser.get();
    }
}
