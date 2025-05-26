package org.my_tma_test_app.tmaz.service;

import org.junit.jupiter.api.Test;
import org.my_tma_test_app.tmaz.TmazApplicationTests;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtServiceTest extends TmazApplicationTests {
    @Autowired
    JwtService jwtService;

    @Test
    void generateJwtToken() {
        TelegramUser user = TelegramUser.builder()
                .id(131504666L)
                .languageCode("en")
                .username("AbyssInsideInYou")
                .firstName("line")
                .photoUrl("https://t.me/i/userpic/320/qPWdOnB0dc3tsyKD24_MJFyzLpLQVua3wdheG99tCC4.svg")
                .build();

        String jwtToken = jwtService.generateJwtToken(user);
        System.out.println("DEBUG JWT TOKEN: " + jwtToken);
    }
}
