package org.my_tma_test_app.tmaz.security.service;


import org.my_tma_test_app.tmaz.security.model.TelegramUser;

import java.util.Optional;

public interface TelegramDataValidatorService {

    Optional<TelegramUser> validate(String initData);
}
