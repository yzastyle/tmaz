package org.my_tma_test_app.tmaz.security.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.my_tma_test_app.tmaz.config.TelegramBotSourceProperties;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;

import java.util.Map;


@Data
public class TelegramDataValidatorServiceImpl implements TelegramDataValidatorService {

    private final String username;
    private final String token;
    private final boolean authDateCheckEnabled;
    private final int authDateMaxAgeSeconds;
    private final String telegramDataCheckKey;
    private final String hmacSha256;


    //private final ObjectMapper objectMapper;

    public TelegramDataValidatorServiceImpl(TelegramBotSourceProperties telegramBotSourceProperties) {
        this.username = telegramBotSourceProperties.getUsername();
        this.token = telegramBotSourceProperties.getToken();
        this.authDateCheckEnabled = telegramBotSourceProperties.isAuthDateCheckEnabled();
        this.authDateMaxAgeSeconds = telegramBotSourceProperties.getAuthDateMaxAgeSeconds();
        this.telegramDataCheckKey = telegramBotSourceProperties.getTelegramDataCheckKey();
        this.hmacSha256 = telegramBotSourceProperties.getHmacSha256();
    }

    private Map<String, String> parseInitData(String initData) {
        return null;
    }
}
