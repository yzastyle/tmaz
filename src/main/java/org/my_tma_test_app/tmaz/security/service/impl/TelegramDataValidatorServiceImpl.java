package org.my_tma_test_app.tmaz.security.service.impl;

import lombok.Data;
import lombok.SneakyThrows;
import org.my_tma_test_app.tmaz.config.TelegramBotSourceProperties;
import org.my_tma_test_app.tmaz.security.exception.ValidationException;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;



@Data
public class TelegramDataValidatorServiceImpl implements TelegramDataValidatorService {

    private final String username;
    private final String token;
    private final boolean authDateCheckEnabled;
    private final long authDateMaxAgeSeconds;
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

    @SneakyThrows
    public Map<String, String> validate(String initData) {
        Map<String, String> data = parseInitData(initData);

        String receivedHash = data.get("hash");
        if (receivedHash == null || receivedHash.isEmpty()) {
            throw new ValidationException("Hash parameter is missing");
        }
        String authDate = data.get("auth_date");
        if (authDate == null || authDate.isEmpty()) {
            throw new ValidationException("auth_date parameter is missing");
        }

        validateAuthDate(Long.parseLong(authDate));

        return parseInitData(initData);
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> data;
        String[] kvArray = initData.split("&");
        data = Arrays.stream(kvArray)
                .map(kv -> kv.split("="))
                .collect(Collectors.toMap(p -> URLDecoder.decode(p[0], StandardCharsets.UTF_8),
                        p -> URLDecoder.decode(p[1], StandardCharsets.UTF_8)));
        return data;
    }

    @SneakyThrows
    private void validateAuthDate(long authDate) {
        if (!authDateCheckEnabled) {
            System.out.println("debug: auth date check is disabled");
            return;
        }

        long currentSeconds = Instant.now().getEpochSecond();
        long diff = currentSeconds - authDate;

        if (diff > authDateMaxAgeSeconds) {
            throw new ValidationException(
                    String.format("Auth date is too old: %d seconds ago (max allowed: %d)",
                            diff, authDateMaxAgeSeconds)
            );
        }
        if (diff < 0) {
            throw new ValidationException("Auth date is in the future");
        }
    }
}
