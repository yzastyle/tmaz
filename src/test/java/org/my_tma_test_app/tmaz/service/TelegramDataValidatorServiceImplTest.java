package org.my_tma_test_app.tmaz.service;

import org.junit.jupiter.api.Test;
import org.my_tma_test_app.tmaz.TmazApplicationTests;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.security.service.impl.TelegramDataValidatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class TelegramDataValidatorServiceImplTest extends TmazApplicationTests {

    @Autowired
    TelegramDataValidatorServiceImpl telegramDataValidatorService;

    @Test
    void telegramUserValidateTest() {
        String initData = "query_id=AAEamtYHAAAAABqa1geHo22F&user=%7B%22id%22%3A131504666%2C%22first_name%22%3A%22line%22%2C%22last_name%22%3A%22%22%2C%22username%22%3A%22AbyssInsideInYou%22%2C%22language_code%22%3A%22en%22%2C%22allows_write_to_pm%22%3Atrue%2C%22photo_url%22%3A%22https%3A%5C%2F%5C%2Ft.me%5C%2Fi%5C%2Fuserpic%5C%2F320%5C%2FqPWdOnB0dc3tsyKD24_MJFyzLpLQVua3wdheG99tCC4.svg%22%7D&auth_date=1748003350&signature=FiPWvG8YE4-0lRzX-u93GecGSARY71C9UjOo9UK2-9WlJ1D-4FRVlJK7QDbl4lVYmQtlxZKd5KB2jPOM9lXzCw&hash=6977dc14f2dffd2ea5a379fe94131d628389e888e75e08d326bb69add1a716e3";
        TelegramUser telegramUser = telegramDataValidatorService.validate(initData).get();

        assertEquals(131504666L, telegramUser.getId());
        assertEquals("AbyssInsideInYou", telegramUser.getUsername());
        assertEquals("line", telegramUser.getFirstName());
        assertEquals("en", telegramUser.getLanguageCode());
        assertEquals("https://t.me/i/userpic/320/qPWdOnB0dc3tsyKD24_MJFyzLpLQVua3wdheG99tCC4.svg", telegramUser.getPhotoUrl());
    }
}
