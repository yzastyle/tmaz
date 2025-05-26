package org.my_tma_test_app.tmaz.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.security.model.InitData;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.security.service.JwtService;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramAuthController {

    private final TelegramDataValidatorService telegramDataValidatorService;
    private final JwtService jwtService;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody InitData initData, HttpServletResponse response) {
        Optional<TelegramUser> telegramUser = telegramDataValidatorService.validate(initData.getInitData());

        if(telegramUser.isEmpty()) {
            return ResponseEntity.status(401).body("Authentication failed");
        }

        String jwtToken = jwtService.generateJwtToken(telegramUser.get());

        Cookie cookie = new Cookie("tg-auth", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(telegramUser.get());
    }
}
