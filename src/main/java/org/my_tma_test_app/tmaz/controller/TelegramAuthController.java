package org.my_tma_test_app.tmaz.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.security.model.InitData;
import org.my_tma_test_app.tmaz.security.model.TelegramUser;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramAuthController {

    private final TelegramDataValidatorService telegramDataValidatorService;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody InitData initData, HttpSession httpSession) {
        System.out.println("INIT DATA TEST:   " + initData.getInitData());

        Optional<TelegramUser> telegramUser = telegramDataValidatorService.validate(initData.getInitData());

        if(telegramUser.isEmpty()) {
            return (ResponseEntity<?>) ResponseEntity.status(401);
        }

        System.out.println(telegramUser);
        httpSession.setAttribute("telegramUser", telegramUser.get());

        return ResponseEntity.ok(telegramUser.get());
    }

}
