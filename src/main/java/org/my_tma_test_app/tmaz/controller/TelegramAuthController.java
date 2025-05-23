package org.my_tma_test_app.tmaz.controller;

import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.security.model.InitData;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramAuthController {

    private final TelegramDataValidatorService telegramDataValidatorService;

    @PostMapping("/auth")
    public void auth(@RequestBody InitData initData) {
        System.out.println("INIT DATA TEST:   " + initData.getInitData());
    }

}
